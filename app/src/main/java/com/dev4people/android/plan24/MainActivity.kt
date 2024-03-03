package com.dev4people.android.plan24

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import java.util.*

private const val PREF_NAME = "MyPrefs"
private const val TIME_KEY = "savedTime"
private const val AUTOSAVE_INTERVAL = 55 * 60 * 1000 // Autosave interval in milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button

    private var isRunning = false
    private var elapsedTime: Long = 0
    private var goalTime: Long = 4 * 60 * 60 * 1000 // 4 hours in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById(R.id.chronometer)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)

        val savedTime = getSavedTime()
        chronometer.base = SystemClock.elapsedRealtime() - savedTime
        elapsedTime = savedTime
        startButton.setOnClickListener {
            startChronometer()
        }

        pauseButton.setOnClickListener {
            pauseChronometer()
        }

        // Schedule the end of day task
        scheduleEndOfDayTask()

        // Start autosave while the app is running
        startAutosave()
    }

    private fun startChronometer() {
        if (!isRunning) {
            chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
            chronometer.start()
            isRunning = true
        }
    }

    private fun pauseChronometer() {
        if (isRunning) {
            chronometer.stop()
            elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
            isRunning = false
            saveTime(elapsedTime) // Save the elapsed time
            checkGoalAchievement(elapsedTime)
        }
    }

    private fun checkGoalAchievement(elapsedTime: Long) {
        if (elapsedTime >= goalTime) {
            // Write code to handle goal achievement
        } else {
            // Increase the goal for the next day
            val remainingTime = goalTime - elapsedTime
            val nextDayGoal = goalTime + (remainingTime * 2)
            // Write code to save the new goal for the next day
        }
    }

    private fun saveTime(elapsedTime: Long) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(TIME_KEY, elapsedTime)
        editor.apply()
    }

    private fun getSavedTime(): Long {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(TIME_KEY, 0L)
    }

    private fun scheduleEndOfDayTask() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)

        val intent = Intent(this, EndOfDayReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun startAutosave() {
        val handler = android.os.Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (isRunning) {
                    saveTime(elapsedTime)
                }
                handler.postDelayed(this, AUTOSAVE_INTERVAL.toLong())
            }
        }, AUTOSAVE_INTERVAL.toLong())
    }
}


