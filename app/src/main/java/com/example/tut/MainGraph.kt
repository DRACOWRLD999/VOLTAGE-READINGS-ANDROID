package com.example.tut

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tut.databinding.ActivityGraphBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.google.firebase.database.*

class MainGraph : AppCompatActivity() {
    private lateinit var chart: LineChart
    private lateinit var binding: ActivityGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chart = binding.lineChart
        chart.animateX(2000) // set animation duration to 2 seconds

        val databaseReference =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                "all"
            )
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val entries = ArrayList<Entry>()
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.getValue(String::class.java)
                    value?.let {
                        entries.add(Entry(entries.size.toFloat(), it.toDouble().toFloat()))
                    }
                }

                val dataSet = LineDataSet(entries, "Graph")
                dataSet.valueTextColor = Color.parseColor("#225C6E")
                dataSet.valueTextColor = Color.BLACK
                val lineData = LineData(dataSet)
                chart.data = lineData
                chart.invalidate()
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "Error: ${databaseError.message}")
            }
        })
    }
}
