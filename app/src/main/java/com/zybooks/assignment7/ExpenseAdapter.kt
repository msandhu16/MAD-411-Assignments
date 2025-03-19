package com.zybooks.assignment7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenseArray: MutableList<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val expense_name: TextView
        val expense_amount: TextView
        val deleteButton: Button
//        val expense_date: TextView


        init {
            // Define click listener for the ViewHolder's View
            expense_name = view.findViewById(R.id.expense_name)
            expense_amount = view.findViewById(R.id.expense_amount)
//            expense_date = view.findViewById(R.id.expense_date)
            deleteButton = view.findViewById(R.id.button2)

            deleteButton.setOnClickListener {
                expense_name.text = "";
                expense_amount.text = "";
            }



        }






    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.expense_name.text =  expenseArray[position].name.toString()
        viewHolder.expense_amount.text =  expenseArray[position].amount.toString()
//        viewHolder.expense_date.text = expenseArray[position].date.toString()
        viewHolder.deleteButton.setOnClickListener {
            expenseArray.removeAt(position)
            this.notifyDataSetChanged()

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = expenseArray.size





}