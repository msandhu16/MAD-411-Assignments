package com.zybooks.assignment7

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var expenseName: EditText
    private lateinit var expenseAmount: EditText
    private lateinit var expenseDate: EditText
    private lateinit var submitButton: Button
    private lateinit var deleteButton: Button



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
//        deleteButton = findViewById(R.id.button2)

        // Set up RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.my_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(expenseArray)
        recyclerView.adapter = expenseAdapter
        setDatePicker()

        submitButton.setOnClickListener {
            showExpense()
        }
    }

    private fun showExpense() {

        var expenseNameText = expenseName.text.toString()
        var expenseAmountText = expenseAmount.text.toString()
        var expenseDateText = expenseDate.text.toString()

        val expenseObject = Expense(
            expenseNameText,expenseAmountText,expenseDateText
        )


        expenseArray.add(expenseObject)
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

}