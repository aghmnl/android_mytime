package com.example.mytime

import android.os.SystemClock
import android.widget.ImageButton
import com.example.mytime.dataClasses.MainChronometerStrings
import com.example.mytime.dataClasses.MainChronometerViews

class MainChronometer(
    mainChronometerStrings: MainChronometerStrings,
    private val resetButton: ImageButton,
    mainChronometerViews: MainChronometerViews,
    ) {
    // Declares and initializes variables for the start state and elapsed time
    private var isCounting = false
    private var elapsedTime = 0L
    private val start = mainChronometerStrings.start
    private val pause = mainChronometerStrings.pause
    private val chronometerView = mainChronometerViews.chronometerView
    private val editMainText = mainChronometerViews.editMainText
    private val mainProgressBar = mainChronometerViews.mainProgressBar
    private val mainStartPauseButton = mainChronometerViews.mainStartPauseButton

    // Stops the main chronometer
    private fun stopMainChronometer() {
        chronometerView.stop()
        isCounting = false
        mainStartPauseButton.setImageResource(R.drawable.ic_play)
        mainStartPauseButton.contentDescription = start
    }

    // Starts the main chronometer
    fun startMainChronometer() {
        chronometerView.start()
        isCounting = true
        mainStartPauseButton.setImageResource(R.drawable.ic_pause)
        mainStartPauseButton.contentDescription = pause
    }

    fun setStartPauseButtonClickListener() {
        mainStartPauseButton.setOnClickListener {
            if (isCounting) {
                // Stops and saves the elapsed time
                elapsedTime = SystemClock.elapsedRealtime() - chronometerView.base
                stopMainChronometer()
            } else {
                // Starts from the elapsed time
                chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime
                startMainChronometer()
            }
        }
    }

    fun setResetButtonClickListener() {
        resetButton.setOnClickListener {
            chronometerView.base = SystemClock.elapsedRealtime()  // Returns to 0 the time shown
            elapsedTime = 0L  // Resets the counter
            stopMainChronometer()
        }
    }

    fun setChronometerTickListener() {
        chronometerView.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometerView.base
            val progress = (elapsedMillis / 1000).toInt()
            mainProgressBar.progress = progress

            // If the progress bar is full, stops the chronometer and resets the elapsed time
//            if (mainProgressBar.progress >= mainProgressBar.max) {
//                elapsedTime = 0L
//                stopMainChronometer()
//            }
        }
    }

    fun setMainChronometer(elapsedTime: Long, text: String) {
        // Sets the main chronometer with the provided elapsed time and text
        this.elapsedTime = elapsedTime
        chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime
        startMainChronometer()
        editMainText.setText(text)
    }
}