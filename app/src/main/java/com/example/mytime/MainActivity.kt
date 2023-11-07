package com.example.mytime

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Declare variables for the chronometer and buttons
    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton

    // Declare and initialize variables for the start state and elapsed time
    private var start = true
    private var elapsedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the chronometer and buttons
        chronometer = findViewById(R.id.chronometer)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)

        // Set an onClickListener for the start/pause button
        startPauseButton.setOnClickListener {
            if (start) {
                // If the start variable is true, start the chronometer
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                chronometer.start()
                start = false
                startPauseButton.setImageResource(R.drawable.baseline_pause_24)
                startPauseButton.contentDescription = getString(R.string.stop)
            } else {
                // If the start variable is false, stop the chronometer and save the elapsed time
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                start = true
                startPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
                startPauseButton.contentDescription = getString(R.string.start)
            }
        }

        // Set an onClickListener for the reset button
        resetButton.setOnClickListener {
            // Reset the chronometer and the start state
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()
            elapsedTime = 0L
            start = true
            startPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
            startPauseButton.contentDescription = getString(R.string.start)
        }

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
                startPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
                startPauseButton.contentDescription = getString(R.string.start)
            }
        }
    }
}