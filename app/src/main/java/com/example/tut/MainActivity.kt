package com.example.tut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.tut.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var Binding: ActivityMainBinding
    private var database: DatabaseReference? = null
    var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase App
        FirebaseApp.initializeApp(this)

        // Inflate layout
        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_landing)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance("https://voltagereadings-default-rtdb.europe-west1.firebasedatabase.app").getReference()

        // Fetch data from Firebase Realtime Database and save it in a variable
        database!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    data = snapshot.value.toString() // store the received data in the variable
                    Log.d("Firebase", "Data received: $data")
                    // Update the UI with the received data
                    Binding.voltage.text = data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Data fetching cancelled: ${error.message}")
            }
        })
        val myBtn=findViewById<Button>(R.id.btnToReadings)
        myBtn.setOnClickListener {
            setContentView(R.layout.activity_main)
        }
    }
}