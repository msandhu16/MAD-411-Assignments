package com.zybooks.assignment7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ExpenseDetailsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)

        val name = arguments?.getString("expense_name")
        val amount = arguments?.getDouble("expense_amount", 0.0)
        val date = arguments?.getString("expense_date")

        val nameTextView : TextView =  view.findViewById(R.id.expense_nameTextView)
        val amountTextView = view.findViewById<TextView>(R.id.expense_amountTextView)
        val dateTextView = view.findViewById<TextView>(R.id.expense_dateTextView)

        nameTextView.text = name
        amountTextView.text = "$$amount"
        dateTextView.text = date
        return view

    }


}