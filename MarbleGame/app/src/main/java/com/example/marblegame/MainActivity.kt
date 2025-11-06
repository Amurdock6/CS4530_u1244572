package com.example.marblegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.marblegame.ui.theme.MarbleGameTheme

class MainActivity : ComponentActivity() {

    private val vm: MarbleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Force dark so onBackground is light
            MarbleGameTheme(darkTheme = true) {
                MarbleScreen(vm)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.start()
    }

    override fun onPause() {
        vm.stop()
        super.onPause()
    }
}
