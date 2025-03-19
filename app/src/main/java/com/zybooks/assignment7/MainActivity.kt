package com.zybooks.assignment7

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        expenseNameText = ""
        expenseAmountText = ""
        expenseDateText = ""



    }

}