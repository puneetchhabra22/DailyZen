package com.major.dailyzen.api

import com.major.dailyzen.models.DailyZenApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyZenAPI {

    @GET("dailyzen")
    suspend fun getDailyZen(
        @Query("date") date : String,
        @Query("version") version : Int = 2
    ) : Response<DailyZenApiResponse>
}