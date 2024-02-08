package com.major.dailyzen.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.major.dailyzen.models.DailyZenApiResponseItem
import com.major.dailyzen.util.DateUtil
import java.util.Date

@Dao
interface DailyZenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyZens(dzItems: List<DailyZenItem>)
    //to insert list of DailyZenItem (each item have date in them, so bulk mapping)

//
//    @Query("DELETE FROM dailyZen WHERE dzDate < :thresholdDate")
//    suspend fun deleteDailyZenOlderDate(thresholdDate: Date)

    @Query("SELECT * FROM dailyZen WHERE dzDate = :date")
    suspend fun getDailyZenFromDB(date: String) : List<DailyZenItem>
}