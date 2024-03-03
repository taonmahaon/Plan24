package com.dev4people.android.plan24

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EndOfDayReceiver : BroadcastReceiver() {

    private val PREF_NAME = "MyPrefs"
    private val SAVED_TIME = "savedTime"
    override fun onReceive(context: Context?, intent: Intent?) {
        val savedTime = getSavedTime(context)
        saveTimeAndDate(context, getCurrentDate(), savedTime)
    }

    private fun saveTimeAndDate(context: Context?, date: String, time: Long) {
        val sharedPreferences = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putLong(date, time)
        editor?.apply()
    }

    private fun getSavedTime(context: Context?): Long {
        val sharedPreferences = context?.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences?.getLong(SAVED_TIME, 0L) ?: 0L
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }


}




