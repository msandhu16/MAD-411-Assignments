package com.zybooks.assignment7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class text_row_item : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.text_row_item)
        val expenseName = findViewById<TextView>(R.id.expense_name)
        val expenseAmount = findViewById<TextView>(R.id.expense_amount)
        val expenseDate = findViewById<TextView>(R.id.expense_date)
        val showDetailsButton = findViewById<Button>(R.id.show_details)

        showDetailsButton.setOnClickListener {
            val intent = Intent(this, ExpenseDetailsActivity::class.java)
            startActivity(intent)

        }



    }
    }
