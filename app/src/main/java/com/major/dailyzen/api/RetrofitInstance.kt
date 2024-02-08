package com.major.dailyzen.api

import com.major.dailyzen.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitInstance { //to create a single instance of API call in the application
    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val dailyZenAPI by lazy {
            retrofit.create(DailyZenAPI::class.java)
        }
    }
}