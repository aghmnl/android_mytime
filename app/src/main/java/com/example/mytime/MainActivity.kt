package com.example.mytime

import android.os.Bundle
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Code moved to other classes for better understanding
    private lateinit var mainChronometer: MainChronometer
    private lateinit var chronometerState: ChronometerState
    private lateinit var secondaryChronometers: SecondaryChronometers

    // Declare variables for the chronometer, buttons and progressbar
    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    // Creates the list of chronometers
    private val chronometers = mutableListOf<Triple<Long, Boolean, String>>()

    // Declare and initialize variables for the start state and elapsed time
    internal var start = true
    internal var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the chronometer, buttons and progressbar
        chronometer = findViewById(R.id.chronometer)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        floatingActionButton = findViewById(R.id.floatingActionButton)

        // Initialize the ChronometerState and the mainChronometer objects
        mainChronometer = MainChronometer(this)
        chronometerState = ChronometerState(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        secondaryChronometers = SecondaryChronometers()
        recyclerView.adapter = ChronometerAdapter(chronometers)

        // Setting the listeners
        mainChronometer.setStartPauseButtonClickListener(startPauseButton, chronometer)
        mainChronometer.setResetButtonClickListener(resetButton, chronometer, startPauseButton)
        mainChronometer.setChronometerTickListener(chronometer, progressBar, startPauseButton)
        secondaryChronometers.setFloatingActionButtonClickListener(floatingActionButton, recyclerView, chronometers)

        // Restore the state from SharedPreferences
        chronometers.addAll(chronometerState.restoreState())
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Save the state to SharedPreferences
//        chronometerState.saveState(chronometers)
//        println("SAVING THE STATE in onDestroy")
//    }

    override fun onPause() {
        super.onPause()

        // Save the state to SharedPreferences
        chronometerState.saveState(chronometers)
        println("SAVING THE STATE in onPause")
    }
}