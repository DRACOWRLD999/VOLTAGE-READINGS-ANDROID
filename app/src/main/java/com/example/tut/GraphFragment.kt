package com.example.tut

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tut.databinding.GraphFragmentBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.google.firebase.database.*

class GraphFragment : Fragment(R.layout.graph_fragment) {
    private lateinit var chart: LineChart
    private lateinit var binding: GraphFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = GraphFragmentBinding.inflate(inflater, container, false)
        chart = binding.lineChart
        chart.animateX(2500)
        // rest of the code
        return binding.root
    }

    private fun setUpChart(entries: ArrayList<Entry>) {
        val lineColor =  Color.parseColor("#e6e600")
        val dataSet = LineDataSet(entries, "MEASUREMENTS")
        val legend = chart.legend
        legend.textSize=10f
        legend.textColor = Color.WHITE
        dataSet.color = lineColor
        // Set up the X axis
        val xAxis = chart.xAxis
        xAxis.granularity = 1f // show grid line every 1 unit on x axis
        xAxis.gridColor = Color.parseColor("#aaaaaa") // set grid line color

// Set up the Y axis
        val yAxis = chart.axisLeft
        val RYaxies=chart.axisRight
        yAxis.granularity = 1f // show grid line every 1 unit on y axis
        yAxis.gridColor = Color.parseColor("#ffffff") // set grid line color

        dataSet.setCircleColor(Color.parseColor("#e6e600"))
        dataSet.valueTextColor = R.color.myColor
        dataSet.valueTextColor = Color.WHITE
        xAxis.textColor = Color.WHITE
        yAxis.textColor = Color.WHITE
        RYaxies.textColor = Color.WHITE

        chart.description.text = "VOLTAGE GRAPH"
        chart.description.textSize=10f
        chart.description.textColor=Color.WHITE
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val databaseReference = FirebaseDatabase.getInstance("https://voltageread-22aa9-default-rtdb.firebaseio.com/").reference.child("voltage")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val entries = ArrayList<Entry>()
                for (snapshot in dataSnapshot.children) {
                    val value = snapshot.value.toString()
                    value.let {
                        entries.add(Entry(entries.size.toFloat(), it.toDouble().toFloat()))
                    }
                }
                setUpChart(entries)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Firebase", "Error: ${databaseError.message}")
            }
        })
    }
}
