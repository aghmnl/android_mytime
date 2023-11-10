package com.example.mytime

import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast

class MainChronometer(private val activity: MainActivity) {

    fun setStartPauseButtonClickListener(startPauseButton: ImageButton, chronometer: Chronometer) {
        // Set an onClickListener for the start/pause button
        startPauseButton.setOnClickListener {
            if (activity.start) {
                // If the start variable is true, start the chronometer
                chronometer.base = SystemClock.elapsedRealtime() - activity.elapsedTime
                chronometer.start()
                activity.start = false
                startPauseButton.setImageResource(R.drawable.ic_pause)
                startPauseButton.contentDescription = activity.getString(R.string.pause)
            } else {
                // If the start variable is false, stop the chronometer and save the elapsed time
                activity.elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                activity.start = true
                startPauseButton.setImageResource(R.drawable.ic_play)
                startPauseButton.contentDescription = activity.getString(R.string.start)
            }
        }
    }

    fun setResetButtonClickListener(resetButton: ImageButton, chronometer: Chronometer, startPauseButton: ImageButton) {
        // Set an onClickListener for the reset button
        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()
            activity.elapsedTime = 0L
            activity.start = true
            startPauseButton.setImageResource(R.drawable.ic_play)
            startPauseButton.contentDescription = activity.getString(R.string.start)
        }
    }

    fun setChronometerTickListener(chronometer: Chronometer, progressBar: ProgressBar, startPauseButton: ImageButton) {
        // Set an onChronometerTickListener for the chronometer
        chronometer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val progress = (elapsedMillis / 1000).toInt() // Convert milliseconds to seconds
            progressBar.progress = progress

            // If the progress bar is full, stop the chronometer and reset the start state
            if (progressBar.progress >= progressBar.max) {
                Toast.makeText(activity, "Time's up!", Toast.LENGTH_SHORT).show()
                chronometer.stop()
                activity.elapsedTime = 0L
                activity.start = true
                startPauseButton.setImageResource(R.drawable.ic_play)
                startPauseButton.contentDescription = activity.getString(R.string.start)
            }
        }
    }
}