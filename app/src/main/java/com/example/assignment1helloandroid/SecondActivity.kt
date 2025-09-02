package com.example.assignment1helloandroid

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Text from MainActivity
        val textFromButton = intent.getStringExtra(MainActivity.EXTRA_BUTTON_CONTENT).orEmpty()
        findViewById<TextView>(R.id.textView).text = textFromButton

        // Back Button Logic
        findViewById<ImageButton>(R.id.imageButton).setOnClickListener {
            // Finishing instead of starting MainActivity again
            finish()
        }
    }
}