package com.major.dailyzen.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.major.dailyzen.models.DailyZenApiResponseItem

class BottomSheetViewModelFactory(private val dailyZenApiResponseItem: DailyZenApiResponseItem, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BottomSheetFragmentViewModel(dailyZenApiResponseItem, context) as T
    }
}