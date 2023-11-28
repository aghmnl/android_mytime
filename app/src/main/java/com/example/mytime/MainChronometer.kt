package com.example.mytime

import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast

class MainChronometer(
        private val activity: MainActivity,
        private val chronometer: Chronometer,
        private val startPauseButton: ImageButton,
        private val resetButton: ImageButton,
        private val progressBar: ProgressBar
    ) {
    // Declares and initializes variables for the start state and elapsed time
    private var isCounting = false
    private var elapsedTime = 0L

    // Stops the main chronometer
    private fun stopMainChronometer() {
        chronometer.stop()
        isCounting = false
        startPauseButton.setImageResource(R.drawable.ic_play)
        startPauseButton.contentDescription = activity.getString(R.string.start)
    }

    // Starts the main chronometer
    private fun startMainChronometer() {
        chronometer.start()
        isCounting = true
        startPauseButton.setImageResource(R.drawable.ic_pause)
        startPauseButton.contentDescription = activity.getString(R.string.pause)
    }

    fun setStartPauseButtonClickListener() {
        startPauseButton.setOnClickListener {
            if (isCounting) {
                // Stops and saves the elapsed time
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                stopMainChronometer()
            } else {
                // Starts from the elapsed time
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                startMainChronometer()
            }
        }
    }

    fun setResetButtonClickListener() {
        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()  // Returns to 0 the time shown
            elapsedTime = 0L  // Resets the counter
            stopMainChronometer()
        }
    }

    fun setChronometerTickListener() {
        chronometer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val progress = (elapsedMillis / 1000).toInt()
            progressBar.progress = progress

            // If the progress bar is full, stops the chronometer and resets the elapsed time
            if (progressBar.progress >= progressBar.max) {
                Toast.makeText(activity, activity.getString(R.string.times_up), Toast.LENGTH_SHORT).show()
                elapsedTime = 0L
                stopMainChronometer()
            }
        }
    }
}