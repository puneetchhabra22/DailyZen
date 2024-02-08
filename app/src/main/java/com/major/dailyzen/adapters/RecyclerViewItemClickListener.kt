package com.major.dailyzen.adapters

import android.view.View
import com.major.dailyzen.models.DailyZenApiResponseItem

interface RecyclerViewItemClickListener {

    fun onShareBtnClickListener(dailyZenApiResponseItem: DailyZenApiResponseItem)

    fun onBookmarkBtnClickListener(view: View)

    fun onReadFullPostBtnClickListner(dailyZenApiResponseItem: DailyZenApiResponseItem)
}