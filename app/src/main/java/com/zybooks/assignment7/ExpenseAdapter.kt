package com.zybooks.assignment7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenseArray: MutableList<Expense>,
    private val mainFragment: MainFragment
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val expense_name: TextView = view.findViewById(R.id.expense_name)
        val expense_amount: TextView = view.findViewById(R.id.expense_amount)
        val expense_date: TextView = view.findViewById(R.id.expense_date)
        val converted_symbol: TextView = view.findViewById(R.id.converted_currency_symbol)
        val converted_amount: TextView = view.findViewById(R.id.converted_currency_number)
        val deleteButton: Button = view.findViewById(R.id.button2)
        val showDetailsButton: Button = view.findViewById(R.id.show_details)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val expense = expenseArray[position]

        viewHolder.expense_name.text = expense.name
        viewHolder.expense_amount.text = "${expense.currency.uppercase()} ${expense.amount} "
        viewHolder.expense_date.text = expense.date

        viewHolder.converted_symbol.text = expense.currency.uppercase() ?: "CAD"
        viewHolder.converted_amount.text = expense.convertedCost.toString()

        viewHolder.deleteButton.setOnClickListener {
            expenseArray.removeAt(position)
            mainFragment.deleteExpense(expense)
            notifyDataSetChanged()
        }

        viewHolder.showDetailsButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("expense_name", expense.name)
                putString("expense_amount", expense.amount)
                putString("expense_date", expense.date)
                putString("expense_currency", expense.currency)
                putDouble("converted_cost", expense.convertedCost)
            }
            viewHolder.itemView.findNavController()
                .navigate(R.id.action_mainFragmentToExpenseDetailFragment, bundle)
        }
    }

    override fun getItemCount() = expenseArray.size
}
