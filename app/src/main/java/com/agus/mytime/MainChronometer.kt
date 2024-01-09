package com.agus.mytime

import android.os.SystemClock
import android.widget.ImageButton
import com.agus.mytime.dataClasses.Chronometer
import com.agus.mytime.dataClasses.MainChronometerStrings
import com.agus.mytime.dataClasses.MainChronometerViews
import com.example.mytime.R


class MainChronometer(
    private val allChronometers: MutableList<Chronometer>,
    mainChronometerStrings: MainChronometerStrings,
    private val resetButton: ImageButton,
    mainChronometerViews: MainChronometerViews,
    ) {
    // Declares and initializes variables for the start state and elapsed time
//    private var isCounting = false
//    private var elapsedTime = 0L
    private val start = mainChronometerStrings.start
    private val pause = mainChronometerStrings.pause
    private val chronometerView = mainChronometerViews.chronometerView
    private val editMainText = mainChronometerViews.editMainText
    private val mainProgressBar = mainChronometerViews.mainProgressBar
    private val mainStartPauseButton = mainChronometerViews.mainStartPauseButton

    fun initialize() {
        editMainText.setText(allChronometers[0].label)
        chronometerView.base = SystemClock.elapsedRealtime() - allChronometers[0].elapsedTime
    }

    fun stop() {
        chronometerView.stop()
        println("VER RESULTADO DE ESTA CUENTA: ${SystemClock.elapsedRealtime() - chronometerView.base}")
        println("elapsed time es ${allChronometers[0].elapsedTime}")
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

    fun setResetButtonClickListener() {
        resetButton.setOnClickListener {
            chronometerView.stop()
            chronometerView.base = SystemClock.elapsedRealtime()  // Returns to 0 the time shown
            allChronometers[0].setTime(0)
            allChronometers[0].setIsCounting(false)
            mainStartPauseButton.setImageResource(R.drawable.ic_play)
            mainStartPauseButton.contentDescription = start
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

//    fun getElapsedTime(): Long {
//        return SystemClock.elapsedRealtime() - chronometerView.base
//    }

//    fun setMainChronometer(elapsedTime: Long, text: String) {
//        // Sets the main chronometer with the provided elapsed time and text
//        this.elapsedTime = elapsedTime
//        chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime
//        startMainChronometer()
//        editMainText.setText(text)
//    }
}