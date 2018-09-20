package com.example.mibeo.w1d35_president_retrofit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lv_presidentList.adapter= PresidentAdapter(this, GlobalModel.presidents)
        lv_presidentList.setOnItemClickListener { _,_, position, _ ->
            tv_president.text = GlobalModel.presidents[position].toString()
            tv_desc.text = GlobalModel.presidents[position].description
            searchPresidentOnWiki(GlobalModel.presidents[position].name)
        }
    }

    private fun searchPresidentOnWiki(presidentName: String) {
        val wikiService = WikiApi.service

        val value = object : Callback<WikiApi.Model.WikiInfo> {
            override fun onFailure(call: Call<WikiApi.Model.WikiInfo>?, t: Throwable?) {
                Log.d("wiki", t.toString())
            }

            override fun onResponse(call: Call<WikiApi.Model.WikiInfo>?, response: Response<WikiApi.Model.WikiInfo>?) {
                if (response != null) {
                    Log.d("wikiTest", response.body().toString())
                    val res : WikiApi.Model.WikiInfo = response.body()!!
                    Log.d("wikiTest", res.query.searchinfo.totalhits.toString())
                    showTotalHits(res)
                }
            }
        }
        wikiService.getInfoFromWiki(presidentName).enqueue(value)
    }

    private fun showTotalHits(presidentInfoFromWiki : WikiApi.Model.WikiInfo) {
        val totalHits = "Wikipedia hits: ${presidentInfoFromWiki.query.searchinfo.totalhits}"
        tv_total_hits.text = totalHits
    }
}
