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

    // Declare variables for the chronometer, buttons and progressbar
    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    // Creates the list of chronometers
    private val chronometers = mutableListOf<Triple<Long, Boolean, String>>()

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
        mainChronometer = MainChronometer(this, chronometer, startPauseButton, resetButton, progressBar)
        chronometerState = ChronometerState(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChronometerAdapter(chronometers)

        // Setting the listeners
        mainChronometer.setStartPauseButtonClickListener()
        mainChronometer.setResetButtonClickListener()
        mainChronometer.setChronometerTickListener()
        floatingActionButton.setOnClickListener {
            chronometers.add(Triple(0, false, ""))
            recyclerView.adapter?.notifyItemInserted(chronometers.size - 1)
        }

        // Restore the state from SharedPreferences
        chronometers.addAll(chronometerState.restoreState())
    }

    override fun onPause() {
        super.onPause()

        // Save the state to SharedPreferences
        chronometerState.saveState(chronometers)
//        println("SAVING THE STATE in onPause")
    }
}