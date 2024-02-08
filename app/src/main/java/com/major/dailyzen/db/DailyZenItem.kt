package com.major.dailyzen.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "dailyZen")
data class DailyZenItem(
    var articleUrl: String,
    val author: String,
    val bgImageUrl: String,
    val dzImageUrl: String,
    val dzType: String,
    val language: String,
    val primaryCTAText: String,
    val sharePrefix: String,
    val text: String,
    val theme: String,
    val themeTitle: String,
    val type: String,
    @PrimaryKey(autoGenerate = false)
    val uniqueId: String,
    val dzDate: String //format - yyyyMMdd
)
