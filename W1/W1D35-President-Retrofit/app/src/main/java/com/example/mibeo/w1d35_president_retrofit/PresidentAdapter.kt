package com.example.mibeo.w1d35_president_retrofit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PresidentAdapter(context: Context, private val presidents: MutableList<President>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.item_president, parent, false)

        val thisPresident = presidents[position]

        var textView = rowView.findViewById<TextView>(R.id.tv_Name)
        textView.text = thisPresident.name

        textView = rowView.findViewById<TextView>(R.id.tv_start)
        textView.text = Integer.toString(thisPresident.startTime)

        textView = rowView.findViewById<TextView>(R.id.tv_end)
        textView.text = Integer.toString(thisPresident.endTime)

        return rowView
    }

    override fun getItem(position: Int): Any {
        return presidents[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return presidents.size
    }

}