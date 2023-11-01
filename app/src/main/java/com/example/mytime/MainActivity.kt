package com.example.mytime

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var startPauseButton: ImageButton
    private lateinit var resetButton: ImageButton

    private var start = true
    private var elapsedTime = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById(R.id.chronometer)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)

        startPauseButton.setOnClickListener {
            if (start) {
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                chronometer.start()
                start = false
                startPauseButton.setImageResource(R.drawable.baseline_pause_24)
                startPauseButton.contentDescription = getString(R.string.stop)
            } else {
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                start = true
                startPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
                startPauseButton.contentDescription = getString(R.string.start)
            }
        }

        resetButton.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.stop()
            elapsedTime = 0L
            start = true
            startPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
            startPauseButton.contentDescription = getString(R.string.start)
        }

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        chronometer.setOnChronometerTickListener {
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val progress = (elapsedMillis / 1000).toInt() // Convert milliseconds to seconds
            progressBar.progress = progress

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