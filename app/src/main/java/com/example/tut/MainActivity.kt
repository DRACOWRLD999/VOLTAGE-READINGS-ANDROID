package com.example.tut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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

        // Initialize Firebase Database query
        val query = FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/")
            .reference
            .child("all")
            .orderByKey()
            .limitToLast(1)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.value != null) {
                    val lastChildSnapshot = snapshot.children.lastOrNull()
                    if (lastChildSnapshot != null) {
                        val data = lastChildSnapshot.value.toString()
                        if (!data.isNullOrEmpty()) {
                            Log.d("Firebase", "Data received: $data")
                            Binding.voltage.text = data.substringAfter('=')
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })



    val myBtn=findViewById<Button>(R.id.btnToReadings)
        myBtn.setOnClickListener {
            setContentView(Binding.root)
            print(data)
        }
    }
}
