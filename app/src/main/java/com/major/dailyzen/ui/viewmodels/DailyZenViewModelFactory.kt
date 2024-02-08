package com.major.dailyzen.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.major.dailyzen.repository.DailyZenRepository

class DailyZenViewModelFactory(val dailyZenRepository: DailyZenRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DailyZenViewModel(dailyZenRepository) as T
    }
}