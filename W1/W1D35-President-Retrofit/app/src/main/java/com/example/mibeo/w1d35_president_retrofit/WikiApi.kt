package com.example.mibeo.w1d35_president_retrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WikiApi {
    //create URL
    val url = "https://en.wikipedia.org/w/"

    //Define models
    object Model {
        data class WikiInfo (var query: Query)
        data class Query (var searchinfo : SearchInfo)
        data class SearchInfo (var totalhits: Int)
    }

    //Define Services
    interface Service {
        @GET("api.php?action=query&format=json&list=search")
        fun getInfoFromWiki(@Query("srsearch") name : String) : Call<Model.WikiInfo>
    }

    //Create and configure retrofit with builder
    private val retrofit = Retrofit.Builder()
            .baseUrl(url) //Add the base url
            .addConverterFactory(GsonConverterFactory.create()) //Assign the GSON converter as the converter
            .build()

    //Create a retrofit service
    val service = retrofit.create(Service::class.java)
}