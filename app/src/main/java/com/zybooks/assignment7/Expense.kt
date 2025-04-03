package com.zybooks.assignment7

import android.icu.util.Currency


class Expense(var name: String, var amount: String, var date: String, val currency: Currency, val convertedCost: Double)