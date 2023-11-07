package com.example.mytime

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Declare variables for the chronometer and buttons
    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private val chronometers = mutableListOf<Pair<Chronometer, ImageButton>>()


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
                startPauseButton.setImageResource(R.drawable.ic_pause)
                startPauseButton.contentDescription = getString(R.string.pause)
            } else {
                // If the start variable is false, stop the chronometer and save the elapsed time
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                start = true
                startPauseButton.setImageResource(R.drawable.ic_play)
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
            startPauseButton.setImageResource(R.drawable.ic_play)
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
                startPauseButton.setImageResource(R.drawable.ic_play)
                startPauseButton.contentDescription = getString(R.string.start)
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        floatingActionButton = findViewById(R.id.floatingActionButton)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChronometerAdapter(chronometers)

        floatingActionButton.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.chronometer_row, recyclerView, false)
            val chronometer = view.findViewById<Chronometer>(R.id.chronometer)
            val startStopButton = view.findViewById<ImageButton>(R.id.startStopButton)

            chronometers.add(Pair(chronometer, startStopButton))
            recyclerView.adapter?.notifyItemInserted(chronometers.size - 1)
        }
    }
}