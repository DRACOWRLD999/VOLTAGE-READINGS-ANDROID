package com.example.tut

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tut.databinding.ReadingFragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.round

class ReadingFragment : Fragment() {
    private lateinit var binding: ReadingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReadingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val databaseRef =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/")
                .reference.child("voltage")

        databaseRef.orderByValue().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val minChildren = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val value = childSnapshot.getValue(String::class.java)
                    if (value != null) minChildren.add(value) }
                val smallestValue = minChildren.minByOrNull { it.toDoubleOrNull() ?: Double.POSITIVE_INFINITY }
                val roundedValue = smallestValue?.toDouble()?.let {round(it * 100) / 100 }
                binding.minVoltage.text = roundedValue?.toString() ?: ""


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
                val biggestValue = maxChildren.maxByOrNull { it.toDoubleOrNull() ?: Double.NEGATIVE_INFINITY }
                val roundedValue = biggestValue?.toDouble()?.let {round(it * 100) / 100 }
                binding.maxVoltage.text = roundedValue?.toString() ?: ""


                Log.d("maxMin", " getting max value :$biggestValue")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("maxMin", "error getting max value")
            }
        })
        val myVoltagesRef =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                "voltage"
            ).orderByKey().limitToLast(1)

        var lastDataTime: Long = 0
        val TIMEOUT_MS = 5000 // 5 seconds

        myVoltagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastChildSnapshot = snapshot.children.last()
                val data = lastChildSnapshot?.value.toString()
                Log.d("Firebase", "Data received: $data")
                binding.voltage.text = data
                lastDataTime = System.currentTimeMillis()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastDataTime >= TIMEOUT_MS) {
                    binding.voltage.text = "0"
                }
                handler.postDelayed(this, TIMEOUT_MS.toLong())
            }
        }
        handler.postDelayed(runnable, TIMEOUT_MS.toLong())

        val myCurrentRef=
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                "current"
            ).orderByKey().limitToLast(1)
        myCurrentRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastChildSnapshot = snapshot.children.last()
                val currentData = lastChildSnapshot?.value?.toString()?.toDoubleOrNull() ?: 0.0
                val currentDataMultiplied =currentData*1000
                val currentRounded= round(currentDataMultiplied*10000)/10000
                binding.tvAmps.text = currentRounded.toString()

                Log.d("Firebase", "Data received: $currentData")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })
        val myTimeRef =
            FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child(
                "time"
            ).orderByKey().limitToLast(1)

        myTimeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastChildSnapshot = snapshot.children.last()
                val timeData = lastChildSnapshot?.value.toString()
                Log.d("Firebase", "Data received: $timeData")
                binding.tvTime.text = timeData
                val freq=1000/timeData.toDouble()
                if (timeData.toDouble()==0.0){
                    binding.tvFreq.text="DC"
//                    binding.tvFreq.textSize= 15F
                }
                else if (freq>1000){
                    binding.freqUnit.setText(R.string.khz)
                    val freqKhz=freq/1000
                    binding.tvFreq.text=freqKhz.toString()
                }
                else binding.tvFreq.text=freq.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })
        myVoltagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastChildSnapshot = snapshot.children.last()
                val data = lastChildSnapshot?.value.toString()
                Log.d("Firebase", "Data received: $data")
                binding.tvLastVoltage.text = data
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", "Error fetching data from Firebase: ${error.message}")
            }
        })
    }
}