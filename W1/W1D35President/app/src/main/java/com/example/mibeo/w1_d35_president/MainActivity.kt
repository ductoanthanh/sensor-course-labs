package com.example.mibeo.w1_d35_president

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun updateTextView(text: String, hit: Int) {
        (supportFragmentManager.findFragmentById(R.id.fragment_text) as? InfoFragment)
                ?.updateText(text, hit)
    }
}
