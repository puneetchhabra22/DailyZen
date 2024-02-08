package com.major.dailyzen.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.major.dailyzen.api.RetrofitInstance
import com.major.dailyzen.db.DailyZenDatabase
import com.major.dailyzen.db.DailyZenItem
import com.major.dailyzen.models.DailyZenApiResponse
import com.major.dailyzen.models.DailyZenApiResponseItem
import com.major.dailyzen.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class DailyZenRepository(private val dailyZenDatabase: DailyZenDatabase) {
    //private mutable live data
    private val dailyZenData : MutableLiveData<DailyZenApiResponse> = MutableLiveData()

    //public live data, to be accessed by ViewModels
    val dailyZen : LiveData<DailyZenApiResponse>
        get() = dailyZenData

    suspend fun getDailyZen(date: String){

        var dailyZen : DailyZenApiResponse = getDailyZenFromDB(date)

        if (dailyZen.isEmpty()){ //no record available for given date in DB. Fetch from API
            val apiResult = getDailyZenFromAPI(date)
            if(apiResult != null){
                dailyZen = apiResult
            } else { //couldn't even get data from API
                //do something (in future)
                //just show animation
            }

            withContext(Dispatchers.IO) {
                //Insert data got from API to DB
                val dailyZenItems = convertToDailyZenItems(dailyZen,date)
                dailyZenDatabase.dailyZenDao().insertDailyZens(dailyZenItems)
            }
        }

        //val dailyZen has data now(from DB or from API), if available or maybe empty

        //Update live data
        dailyZenData.postValue(dailyZen)
    }

    //to get daily zen data for "date" from API
    private suspend fun getDailyZenFromAPI(date : String): DailyZenApiResponse?{
        Log.d("DATA", "Getting data from api")
        val response = RetrofitInstance.dailyZenAPI.getDailyZen(date)
        if(response.isSuccessful){
            return response.body()!!
        } else {
            Log.d("DATA","Couldn't get data from API to the repository: ${response.message()}")
            return null //return null if Couldn't get data from API
        }
    }

    private suspend fun getDailyZenFromDB(date: String) : DailyZenApiResponse{
        Log.d("DATA", "Getting data from DB for dateString: $date")
        val dailyZenItems = dailyZenDatabase.dailyZenDao().getDailyZenFromDB(date)
        Log.d("DATA", "From DB dailyZenItems: $dailyZenItems")
        val dailyZenApiResponse = convertToDailyZenApiResponse(dailyZenItems)
        return dailyZenApiResponse
    }

    //add date to DailyZenApiResponseItem and convert it to
    private fun convertToDailyZenItems(dailyZens : DailyZenApiResponse, dzDate: String) : MutableList<DailyZenItem>{
        val dailyZenItems : MutableList<DailyZenItem> = mutableListOf()
        for(items in dailyZens){
            val dailyZenItem = DailyZenItem(items.articleUrl,items.author,items.bgImageUrl,items.dzImageUrl,items.dzType,items.language,items.primaryCTAText,items.sharePrefix,items.text,items.theme,items.themeTitle,items.type,items.uniqueId,dzDate)
            dailyZenItems.add(dailyZenItem)
        }
        return dailyZenItems
    }

    //remove date from DailyZenItem and convert it to DailyZenApiResponse
    private fun convertToDailyZenApiResponse(dailyZenItems: List<DailyZenItem>) : DailyZenApiResponse{
        val dailyZenApiResponseItems : DailyZenApiResponse = DailyZenApiResponse()
        for(items in dailyZenItems){
            val dailyZenApiResponseItem = DailyZenApiResponseItem(items.articleUrl,items.author,items.bgImageUrl,items.dzImageUrl,items.dzType,items.language,items.primaryCTAText,items.sharePrefix,items.text,items.theme,items.themeTitle,items.type,items.uniqueId)
            dailyZenApiResponseItems.add(dailyZenApiResponseItem)
        }
        return dailyZenApiResponseItems
    }

    //sets mutableLiveData to empty list
    fun emptyListData() {
        dailyZenData.postValue(DailyZenApiResponse())
    }


    //TODO - implement database delte also
}