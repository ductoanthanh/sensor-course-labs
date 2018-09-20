package com.example.mibeo.w2d2bluetooth

import android.bluetooth.le.ScanResult
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ResultListAdapter(context: Context, private var results: HashMap<String, ScanResult>): BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var keyList: List<String> = results.keys.toList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.row_view_results, parent, false)

        val thisResult: ScanResult = results[keyList[position]]!!

        var textView = rowView.findViewById<TextView>(R.id.tv_device_name)
        textView.text = thisResult.device.name

        textView = rowView.findViewById<TextView>(R.id.tv_address)
        textView.text = thisResult.device.address

        textView = rowView.findViewById<TextView>(R.id.tv_strength)
        textView.text = "${thisResult.rssi.toString()} dBm"

        return rowView
    }

    override fun getItem(position: Int): Any {
        return results[keyList[position]]!!

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return results.size
    }

    fun updateResults(newResults: HashMap<String, ScanResult>) {
        results = newResults
        keyList = results.keys.toList()
    }
}