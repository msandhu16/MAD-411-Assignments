package com.zybooks.assignment7

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ExpenseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)
        val name = intent.getStringExtra("expense_name")
        val amount = intent.getDoubleExtra("expense_amount", 0.0)
        val date = intent.getStringExtra("expense_date")

        val nameTextView = findViewById<TextView>(R.id.expense_nameTextView)
        val amountTextView = findViewById<TextView>(R.id.expense_amountTextView)
        val dateTextView = findViewById<TextView>(R.id.expense_dateTextView)

        nameTextView.text = name
        amountTextView.text = "$$amount"
        dateTextView.text = date
        }
    }
