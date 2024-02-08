package com.major.dailyzen

import android.app.Application
import com.major.dailyzen.db.DailyZenDatabase
import com.major.dailyzen.repository.DailyZenRepository

class DailyZenApplication : Application() {

    lateinit var dailyZenRepository: DailyZenRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val dailyZenDatabase = DailyZenDatabase(this)
        dailyZenRepository = DailyZenRepository(dailyZenDatabase)
    }
}