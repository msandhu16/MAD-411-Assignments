package com.zybooks.assignment7.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.zybooks.assignment7.Expense
import com.zybooks.assignment7.R
import java.io.File

class OverdueCheckService : Service() {
    private val channelId = "OverdueTaskChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildInitialNotification())
        //Check right away
        checkForOverdueTasks()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //this is optional, however it is used to check for overdue tasks
        return START_NOT_STICKY
    }

    private fun checkForOverdueTasks() {
        val tasks = loadExpensesFromFile()
        val overdueCount = tasks.count { it.overdue }
        tasks.forEach {
            println("Expense: ${it.name}, Date: ${it.date}, Overdue: ${it.overdue}")
        }
        Log.d("OverdueCheckService", "All expenses: $tasks")


        if (overdueCount > 0) {
            //Building what the notif looks like
            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Overdue Expense Reminder")
                .setContentText("You have $overdueCount overdue expense(s).")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(2, notification)
        }
    }

    private fun buildInitialNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Expense Manager Running")
            .setContentText("Checking for overdue expenses...")
            .setSmallIcon(R.drawable.ic_stat_name)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Overdue Expenses Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun loadExpensesFromFile(): List<Expense> {
        val file = File(filesDir, "expense.txt")
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<Expense>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}