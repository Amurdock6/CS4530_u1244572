package com.example.assignment1helloandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button1: Button = findViewById(R.id.button)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)

        button1.setOnClickListener {
            val buttonText = button1.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("BUTTON_CONTENT", buttonText) // "BUTTON_CONTENT" is the key
            startActivity(intent)
        }

        button2.setOnClickListener {
            val buttonText = button2.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("BUTTON_CONTENT", buttonText) // "BUTTON_CONTENT" is the key
            startActivity(intent)
        }

        button3.setOnClickListener {
            val buttonText = button3.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("BUTTON_CONTENT", buttonText) // "BUTTON_CONTENT" is the key
            startActivity(intent)
        }

        button4.setOnClickListener {
            val buttonText = button4.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("BUTTON_CONTENT", buttonText) // "BUTTON_CONTENT" is the key
            startActivity(intent)
        }

        button5.setOnClickListener {
            val buttonText = button5.text.toString()

            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("BUTTON_CONTENT", buttonText) // "BUTTON_CONTENT" is the key
            startActivity(intent)
        }
    }
}