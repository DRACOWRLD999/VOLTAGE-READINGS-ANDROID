package com.example.tut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tut.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase App
        FirebaseApp.initializeApp(this)

        // Inflate layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToReadings.setOnClickListener { it ->
            it.animate().apply {
                duration = 100
                scaleX(0.95f)
                scaleY(0.95f)
                withEndAction {
                    it.animate().apply {
                        duration = 100
                        scaleX(1f)
                        scaleY(1f)
                        start()
                    }
                    Intent( this@MainActivity , MainActivity2::class.java).also {
                        startActivity(it)
                    }
                }
                start()
            }
        }


    }
}
