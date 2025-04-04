package com.zybooks.assignment7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ExpenseDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)

        val name = arguments?.getString("expense_name")
        val amount = arguments?.getString("expense_amount")
        val date = arguments?.getString("expense_date")
        val currency = arguments?.getString("expense_currency") ?: ""
        val convertedCost = arguments?.getDouble("converted_cost", 0.0)

        val nameTextView: TextView = view.findViewById(R.id.expense_nameTextView)
        val amountTextView: TextView = view.findViewById(R.id.expense_amountTextView)
        val dateTextView: TextView = view.findViewById(R.id.expense_dateTextView)
        val convertedSymbol: TextView = view.findViewById(R.id.converted_currency_symbol)
        val convertedAmount: TextView = view.findViewById(R.id.converted_currency_number)

        nameTextView.text = name
        amountTextView.text = "$amount"
        dateTextView.text = date
        convertedSymbol.text = currency.uppercase()
        convertedAmount.text = String.format("%.2f", convertedCost)

        return view
    }
}
