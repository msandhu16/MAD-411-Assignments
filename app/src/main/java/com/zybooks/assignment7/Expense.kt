package com.zybooks.assignment7


data class Expense(var name: String, var amount: String, var date: String, val currency: String, val convertedCost: Double,val overdue: Boolean)