package com.example.mibeo.w3_bluetoothscanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.mibeo.w3_bluetoothscanner.MainActivity.Companion.EXTRA_MESSAGE
import kotlinx.android.synthetic.main.activity_graph.*

class GraphActivity : AppCompatActivity() {
    lateinit var heartRates: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        // Get the Intent that started this activity and extract the string
        heartRates = intent.getIntegerArrayListExtra(EXTRA_MESSAGE)

        val dataPoints = Array(heartRates.size) { DataPoint(it.toDouble(), heartRates[it].toDouble()) }

        graph_heartRate.title = "Heart beat rate"
        graph_heartRate.gridLabelRenderer.verticalAxisTitle = "Bpm"
        graph_heartRate.gridLabelRenderer.horizontalAxisTitle = "Second"
        graph_heartRate.viewport.isXAxisBoundsManual = true
        graph_heartRate.viewport.setMaxX(5.0)
        graph_heartRate.viewport.setMinX(0.0)
        graph_heartRate.viewport.isScrollable = true
        graph_heartRate.viewport.isScalable = true


        graph_heartRate.addSeries(LineGraphSeries<DataPoint>(dataPoints))

    }
}