package com.major.dailyzen.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DailyZenItem::class] , version = 1)
abstract class DailyZenDatabase: RoomDatabase() {

    abstract fun dailyZenDao(): DailyZenDao

    companion object{ //to follow singleton pattern
        @Volatile
        private var instance: DailyZenDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
            instance?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DailyZenDatabase::class.java,
                "DailyZen_db.db"
            ).build()

    }
}