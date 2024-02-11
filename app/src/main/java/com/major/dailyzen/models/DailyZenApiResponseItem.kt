package com.major.dailyzen.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyZenApiResponseItem(
    val articleUrl: String,
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
    val uniqueId: String
) : Parcelable