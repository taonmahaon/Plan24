package com.dev4people.android.plan24

import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button

    private var isRunning = false
    private var elapsedTime: Long = 0

    private val goalTime: Long = 4 * 60 * 60 * 1000 // 4 hours in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById(R.id.chronometer)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)

        startButton.setOnClickListener {
            startChronometer()
        }

        pauseButton.setOnClickListener {
            pauseChronometer()
        }
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

            checkGoalAchievement(elapsedTime)
        }
    }

    private fun checkGoalAchievement(elapsedTime: Long) {
        if (elapsedTime >= goalTime) {
            // Записываем время, так как цель достигнута
            // TODO: Сохранение времени
        } else {
            // Увеличиваем цель на следующий день
            val remainingTime = goalTime - elapsedTime
            val nextDayGoal = goalTime + (remainingTime * 2)
            // TODO: Сохранение новой цели на следующий день
        }
    }
}