package com.agus.mytime

import android.os.SystemClock
import com.agus.mytime.dataClasses.Chronometer
import com.agus.mytime.dataClasses.MainChronometerStrings
import com.agus.mytime.dataClasses.MainChronometerViews
import com.example.mytime.R

class MainChronometer(
    private val allChronometers: MutableList<Chronometer>,
    mainChronometerStrings: MainChronometerStrings,
    mainChronometerViews: MainChronometerViews,
    ) {
    // Declares and initializes variables for the start state and elapsed time
    private val start = mainChronometerStrings.start
    private val pause = mainChronometerStrings.pause
    private val chronometerView = mainChronometerViews.chronometerView
    private val editMainText = mainChronometerViews.editMainText
    private val mainProgressBar = mainChronometerViews.mainProgressBar
    private val mainStartPauseButton = mainChronometerViews.mainStartPauseButton
    private val resetButton = mainChronometerViews.resetButton
//    private val removeButton = mainChronometerViews.removeButton

    fun initialize() {
        chronometerView.base = SystemClock.elapsedRealtime() - allChronometers[0].elapsedTime
        if (allChronometers[0].isCounting) start()
        editMainText.setText(allChronometers[0].label)
    }

    // Stops the main chronometer
    fun stop() {
        chronometerView.stop()
        allChronometers[0].setTime(SystemClock.elapsedRealtime() - chronometerView.base)
        allChronometers[0].setIsCounting(false)
        mainStartPauseButton.setImageResource(R.drawable.ic_play)
        mainStartPauseButton.contentDescription = start
    }

    // Starts the main chronometer
    fun start() {
        chronometerView.base = SystemClock.elapsedRealtime() - allChronometers[0].elapsedTime
        chronometerView.start()
        allChronometers[0].setIsCounting(true)
        mainStartPauseButton.setImageResource(R.drawable.ic_pause)
        mainStartPauseButton.contentDescription = pause
    }

    fun setStartPauseButtonClickListener() {
        mainStartPauseButton.setOnClickListener {
            if (allChronometers[0].isCounting) {
                stop()
            } else {
                start()
            }
        }
    }

    private fun remove() {
        allChronometers.removeAt(0)
        if (allChronometers.size > 0) initialize()
        // TODO add the reference to the string new-project
        else allChronometers.add(Chronometer(0, false, "New Project"))
    }

//    fun setRemoveButtonClickListener() {
//        removeButton.setOnClickListener {
//            remove()
//        }
//    }


    fun setResetButtonClickListener() {
        resetButton.setOnClickListener {
            chronometerView.base = SystemClock.elapsedRealtime()  // Returns to 0 the time shown
            stop()
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
}