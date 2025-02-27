package com.zybooks.assignment6

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var userName: EditText
    private lateinit var userNameTextView: TextView
    private lateinit var showNameButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userName = findViewById(R.id.user_name)
        showNameButton = findViewById(R.id.show_name)
        userNameTextView = findViewById(R.id.display_name)

        showNameButton.setOnClickListener {
            showName()
        }
    }

    private fun showName() {
        val name = userName.text.toString().trim()

        if (name.isNotEmpty()) {
            userNameTextView.text = "Hello, $name!"
        } else {
            userNameTextView.text = "Please enter your name."
        }
    }
}
