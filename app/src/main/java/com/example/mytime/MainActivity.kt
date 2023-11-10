package com.example.mytime

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Code moved to other classes for better understanding
    private lateinit var mainChronometer: MainChronometer

    //     Declare variables for the chronometer and buttons
    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private val chronometers = mutableListOf<Triple<Long, Boolean, String>>()
    private lateinit var chronometerState: ChronometerState


    // Declare and initialize variables for the start state and elapsed time
    internal var start = true
    internal var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the ChronometerState object
        chronometerState = ChronometerState(this)

        // Initialize the chronometer and buttons
        chronometer = findViewById(R.id.chronometer)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)

        mainChronometer = MainChronometer(this)
        mainChronometer.setStartPauseButtonClickListener(startPauseButton, chronometer)
        mainChronometer.setResetButtonClickListener(resetButton, chronometer, startPauseButton)

        // Initialize the progress bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Set an onChronometerTickListener for the chronometer
        chronometer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val progress = (elapsedMillis / 1000).toInt() // Convert milliseconds to seconds
            progressBar.progress = progress

            // If the progress bar is full, stop the chronometer and reset the start state
            if (progressBar.progress >= progressBar.max) {
                Toast.makeText(this, "Time's up!", Toast.LENGTH_SHORT).show()
                chronometer.stop()
                elapsedTime = 0L
                start = true
                startPauseButton.setImageResource(R.drawable.ic_play)
                startPauseButton.contentDescription = getString(R.string.start)
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChronometerAdapter(chronometers)

        // Restore the state from SharedPreferences
        chronometers.addAll(chronometerState.restoreState())



        floatingActionButton.setOnClickListener {
            chronometers.add(Triple(SystemClock.elapsedRealtime(), false, ""))
            recyclerView.adapter?.notifyItemInserted(chronometers.size - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // Save the state to SharedPreferences
        chronometerState.saveState(chronometers)
        println("SAVING THE STATE in onDestroy")
    }

    override fun onPause() {
        super.onPause()

        // Save the state to SharedPreferences
        chronometerState.saveState(chronometers)
        println("SAVING THE STATE in onPause")
    }
}