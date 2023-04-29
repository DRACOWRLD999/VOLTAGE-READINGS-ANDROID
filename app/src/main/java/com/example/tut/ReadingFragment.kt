package com.example.tut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tut.databinding.ReadingFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ReadingFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ReadingFragmentBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val databaseRef =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/")
                .reference.child("all")

        databaseRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val minChildren = mutableListOf<String>()
                for (chilSnapshot in snapshot.children) {
                    val value = chilSnapshot.getValue(String::class.java)
                    if (value != null) minChildren.add(value)
                }
                val smallestValue =
                    minChildren.minByOrNull { it.toDoubleOrNull() ?: Double.POSITIVE_INFINITY }
                binding.minVoltage.text = smallestValue.toString()

                Log.d("maxMin", " getting min value: $smallestValue")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("maxMin", "error getting min value")
            }
        })

        databaseRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val maxChildren = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val value = childSnapshot.getValue(String::class.java)
                    if (value != null) maxChildren.add(value)
                }
                val biggestValue =
                    maxChildren.maxByOrNull { it.toDoubleOrNull() ?: Double.NEGATIVE_INFINITY }
                binding.maxVoltage.text = biggestValue.toString()

                Log.d("maxMin", " getting max value :$biggestValue")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("maxMin", "error getting max value")
            }
        })
        val allQuery =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                "all"
            ).orderByKey().limitToLast(1)

        allQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastChildSnapshot = snapshot.children.last()
                val data = lastChildSnapshot?.value.toString()
                Log.d("Firebase", "Data received: $data")
                binding.voltage.text = data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })

    }
}