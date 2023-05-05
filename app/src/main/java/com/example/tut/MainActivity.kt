package com.example.tut

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tut.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp


class MainActivity:AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        try {
            setContentView(binding.root)
            FirebaseApp.initializeApp(this)
            val readingFragment = ReadingFragment()
            val graphFragment = GraphFragment()
            val aboutUsFragment = AboutUsFragment()

            setCurrentFragment(readingFragment)

            binding.bottomAppBar.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.miReadings -> setCurrentFragment(readingFragment)
                    R.id.miGraph -> setCurrentFragment(graphFragment)
                    R.id.miAbout -> setCurrentFragment(aboutUsFragment)
                }

                true
            }
        } catch (e: Exception) {
            Toast.makeText(this,"$e",Toast.LENGTH_LONG).show()
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }
}
