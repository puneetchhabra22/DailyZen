package com.major.dailyzen.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    companion object {
        lateinit var parsedCurrentDate: Date
        private val formatter = SimpleDateFormat("yyyyMMdd", Locale.US)
        private val formatterForDisplay = SimpleDateFormat("MMMM, dd'th'", Locale.US)

        //Using Calendar class to support all API levels, instead of LocalDate class

        fun getCurrentTimeString(): String {
            val time = Calendar.getInstance().time
            val dateString = formatter.format(time)
            parsedCurrentDate = formatter.parse(dateString)!!
            return dateString
        }

        fun getPreviousDate(currDate: Date) : Date{
            val calendar = Calendar.getInstance()
            calendar.time = currDate
            calendar.add(Calendar.DATE,-1)
            return calendar.time
        }
        fun getNextDate(currDate: Date) : Date{
            val calendar = Calendar.getInstance()
            calendar.time = currDate
            calendar.add(Calendar.DATE,1)
            return calendar.time
        }

        fun getSevenDaysAgoDate(): Date {
            val calender = Calendar.getInstance()
            calender.add(Calendar.DATE, -7)
            val sevenDaysAgoDate = calender.time
            return sevenDaysAgoDate
        }

        fun getYesterday(): Date {
            val calender = Calendar.getInstance()
            calender.add(Calendar.DATE, -1)
            val yesterday = calender.time
            return yesterday
        }

        fun parseStringToDate(stringDate: String): Date {
            return formatter.parse(stringDate)!!
        }

        fun formatDateToString(date: Date): String {
            return formatter.format(date)
        }

        fun formatDateToDisplay(date: Date): String {
            return formatterForDisplay.format(date)
        }
    }
}