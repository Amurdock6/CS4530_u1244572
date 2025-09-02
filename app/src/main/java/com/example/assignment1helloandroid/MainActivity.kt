package com.example.assignment1helloandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_BUTTON_CONTENT = "EXTRA_BUTTON_CONTENT"
    }

    // Handle logic sending data to second activity.
    private val navigateClickListener = View.OnClickListener { view ->
        val text = (view as Button).text.toString()
        startActivity(
            Intent(this, SecondActivity::class.java)
                .putExtra(EXTRA_BUTTON_CONTENT, text)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle onClick logic for all UI buttons
        listOf(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button5)
            .forEach { id -> findViewById<Button>(id).setOnClickListener(navigateClickListener) }
    }
}