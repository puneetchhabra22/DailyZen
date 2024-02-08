package com.major.dailyzen.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.major.dailyzen.models.DailyZenApiResponse
import com.major.dailyzen.repository.DailyZenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.major.dailyzen.util.DateUtil
import kotlinx.coroutines.CoroutineExceptionHandler

class DailyZenViewModel(private val dailyZenRepository: DailyZenRepository) : ViewModel(){
    val dateToady = DateUtil.getCurrentTimeString() //Toady's date in yyyyMMdd format

    val dateActive = MutableLiveData<String>(dateToady) //date of which user is viewing DZ
    lateinit var activeDateObserver : Observer<String>

    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable -> throwable.printStackTrace() }

    init {
        //on initialisation of this ViewModel
        getDailyZen(dateToady)

        //if dateActive Changes, get DailyZen
        activeDateObserver = Observer<String>{newDate->
            getDailyZen(newDate)
        }
        dateActive.observeForever(activeDateObserver)
    }

    //it gets daily zen and will update live data "dailyZen" in repository
    private fun getDailyZen(date: String){
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.d("DATA", "currentDateString: $date")
            dailyZenRepository.getDailyZen(date)
        }
    }

    //public live data that is linked to "dailyZen" live data in repository
    val dailyZen: LiveData<DailyZenApiResponse>
        get() = dailyZenRepository.dailyZen


    override fun onCleared() {
        super.onCleared()
        dateActive.removeObserver(activeDateObserver)
    }
}