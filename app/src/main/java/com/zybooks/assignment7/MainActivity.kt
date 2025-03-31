package com.zybooks.assignment7

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.google.gson.reflect.TypeToken
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var expenseName: EditText
    private lateinit var expenseAmount: EditText
    private lateinit var expenseDate: EditText
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button
    private lateinit var implicitButton: Button
    private val FILE_NAME = "expense.txt"



    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenseArray = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ActivityLifecycle", "onCreate called")


        expenseName = findViewById(R.id.expenseName)
        expenseAmount = findViewById(R.id.expenseAmount)
        expenseDate = findViewById(R.id.expenseDate)
        submitButton = findViewById(R.id.button)
        implicitButton = findViewById(R.id.implicitIntent)

        // Set up RecyclerView and Adapter
        expenseArray.addAll(loadTasksFromFile(this))
        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(expenseArray,this)
        recyclerView.adapter = expenseAdapter
        setDatePicker()

        submitButton.setOnClickListener {
            showExpense()
        }

        implicitButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.canada.ca/en/financial-consumer-agency/services/covid-19-managing-financial-health.html")
            }
            startActivity(intent)
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.headerFragmentContainer, HeaderFragment())
        transaction.commit()




    }

    private fun showExpense() {

        var expenseNameText = expenseName.text.toString()
        var expenseAmountText = expenseAmount.text.toString()
        var expenseDateText = expenseDate.text.toString()

        val expenseObject = Expense(
            expenseNameText,expenseAmountText,expenseDateText
        )


        expenseArray.add(expenseObject)
        saveTasksToFile(this,expenseArray)
        expenseAdapter.notifyDataSetChanged()

        expenseName.text.clear()
        expenseAmount.text.clear()
        expenseDate.text.clear()



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
                this, datePicker,
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
            e.printStackTrace()        }
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
            e.printStackTrace()        }
        catch (e: IOException) {
            e.printStackTrace()        }
        return expenses
    }

}