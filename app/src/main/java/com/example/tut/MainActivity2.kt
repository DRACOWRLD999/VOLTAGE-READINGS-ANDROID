package com.example.tut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tut.databinding.ActivityMain2Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)

        setContentView(binding.root)
        // Order the child nodes by their values
        val query =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                    "all"
                ).orderByValue()
        // Get the minimum value by retrieving the first child node
        query.limitToFirst(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.childrenCount > 0) {
                    val firstChild = snapshot.children.firstOrNull()
                    val minVal = firstChild?.value
                    binding.minVoltage.text = minVal.toString()
                } else {
                    Log.e("maxMin", "error getting min value")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("maxMin", "error getting min value")
            }
        })

        // Get the maximum value by retrieving the last child node
        query.limitToLast(1).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.childrenCount > 0) {
                    val lastChild = snapshot.children.lastOrNull()
                    val maxVal = lastChild?.value
                    binding.maxVoltage.text = maxVal.toString()
                } else {
                    Log.e("maxMin", "error getting max value")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("maxMin", "error getting max value")
            }
        })
        // Initialize Firebase Database query
        val allQuery =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                    "all"
                ).orderByKey().limitToLast(1)

        allQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.value != null) {
                    val lastChildSnapshot = snapshot.children.lastOrNull()
                    if (lastChildSnapshot != null) {
                        val data = lastChildSnapshot.value.toString()
                        Log.d("Firebase", "Data received: $data")
                        binding.voltage.text = data
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })
        val btnGraph = binding.btnToGraph
        btnGraph.setOnClickListener { it ->
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
                Intent(this@MainActivity2, MainGraph::class.java).also {
                    startActivity(it)
                }
                }
                start()
            }
        }

    }
}