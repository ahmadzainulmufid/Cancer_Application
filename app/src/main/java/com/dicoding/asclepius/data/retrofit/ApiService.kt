package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.information.InformationCancer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    fun getHealthNews(
        @Query("q") query: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") language: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<InformationCancer>
}