package com.zybooks.assignment7

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zybooks.assignment7.network.RetrofitInstance
import com.zybooks.assignment7.service.OverdueCheckService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private lateinit var expenseName: EditText
    private lateinit var expenseAmount: EditText
    private lateinit var expenseDate: EditText
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button
    private lateinit var implicitButton: Button
    private lateinit var currencySpinner: Spinner
    private lateinit var conversionCheckBox: CheckBox

    private val FILE_NAME = "expense.txt"
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenseArray = mutableListOf<Expense>()
    private var currencyMap = mapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        Log.d("ActivityLifecycle", "onCreate called")

        expenseName = view.findViewById(R.id.expenseName)
        expenseAmount = view.findViewById(R.id.expenseAmount)
        expenseDate = view.findViewById(R.id.expenseDate)
        submitButton = view.findViewById(R.id.button)
        implicitButton = view.findViewById(R.id.implicitIntent)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        conversionCheckBox = view.findViewById(R.id.conversionNeededCheckBox)

        setupCurrencySpinner()

        expenseArray.clear()
        expenseArray.addAll(loadTasksFromFile(requireContext()))

        val recyclerView: RecyclerView = view.findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseAdapter = ExpenseAdapter(expenseArray, this)
        recyclerView.adapter = expenseAdapter

        setDatePicker()

        submitButton.setOnClickListener { showExpense() }

        implicitButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.canada.ca/en/financial-consumer-agency/services/covid-19-managing-financial-health.html")
            }
            startActivity(intent)
        }

        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.headerFragmentContainer, HeaderFragment()).commit()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serviceIntent = Intent(requireContext(), OverdueCheckService::class.java)
        ContextCompat.startForegroundService(requireContext(), serviceIntent)



    }

    private fun setupCurrencySpinner() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getCurrencyCode()
                }
                currencyMap = response
                val currencyList = response.keys.sorted()
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyList)
                currencySpinner.adapter = adapter
                currencySpinner.setSelection(currencyList.indexOf("cad"))
            } catch (e: Exception) {
                Snackbar.make(requireView(), "Error fetching currencies", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showExpense() {
        val name = expenseName.text.toString()
        val amountText = expenseAmount.text.toString()
        val date = expenseDate.text.toString()
        val selectedCurrency = currencySpinner.selectedItem.toString()
        val overdue = isOverdue(date)

        if (name.isEmpty() || amountText.isEmpty() || date.isEmpty()) {
            Snackbar.make(requireView(), "All fields must be filled", Snackbar.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()

        lifecycleScope.launch {
            val convertedAmount = if (conversionCheckBox.isChecked) getConvertedAmount(selectedCurrency, amount) else amount

            val expense = Expense(name, amountText, date, selectedCurrency, convertedAmount,overdue)
            expenseArray.add(expense)
            saveTasksToFile(requireContext(), expenseArray)
            expenseAdapter.notifyDataSetChanged()

            expenseName.text.clear()
            expenseAmount.text.clear()
            expenseDate.text.clear()

            Snackbar.make(requireView(), "Expense saved.", Snackbar.LENGTH_LONG).show()
        }
    }
    private fun isOverdue(dueDateStr: String): Boolean {
        return try {
            val sdf = android.icu.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dueDate = sdf.parse(dueDateStr)
            val today = Date()
            dueDate != null && dueDate.before(today)
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun getConvertedAmount(currencyCode: String, baseAmount: Double): Double {
        return try {
            val response = withContext(Dispatchers.IO) {
                RetrofitInstance.api.getPrice()
            }
            ((baseAmount / response.cad[currencyCode.lowercase()]!!) ?: (1.0 * baseAmount))
        } catch (e: Exception) {
            Snackbar.make(requireView(), "Conversion failed.", Snackbar.LENGTH_LONG).show()
            baseAmount
        }
    }

    private fun setDatePicker() {
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar)
        }

        expenseDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.CANADA)
        expenseDate.setText(sdf.format(calendar.time))
    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")
    }

    fun saveTasksToFile(context: Context, expenses: List<Expense>) {
        try {
            val json = Gson().toJson(expenses)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadTasksFromFile(context: Context): MutableList<Expense> {
        val expenses: MutableList<Expense> = mutableListOf()
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return expenses

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loadedTasks: List<Expense> = Gson().fromJson(json, type)
            expenses.addAll(loadedTasks)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return expenses
    }
    fun deleteExpense(expense : Expense){
        expenseArray.remove(expense)
        expenseAdapter.notifyDataSetChanged()
        saveTasksToFile(requireContext(),expenseArray)
    }

}
