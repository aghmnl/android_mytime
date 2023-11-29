package com.example.mytime

import android.os.Bundle
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytime.dataClasses.MainChronometerStrings
import com.example.mytime.dataClasses.MainChronometerViews
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Code moved to other classes for better understanding
    private lateinit var chronometersStoredState: ChronometersStoredState
    private lateinit var mainChronometer: MainChronometer
    private lateinit var secondaryChronometers: SecondaryChronometers
    private lateinit var mainChronometerViews: MainChronometerViews
    // Declare variables for the chronometer, buttons and progressbar
    private lateinit var chronometerView: Chronometer
    private lateinit var editMainText: EditText
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainStartPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    // Creates the list of chronometers
    private val allChronometers = mutableListOf<Triple<Long, Boolean, String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Restores the state from SharedPreferences
        chronometersStoredState = ChronometersStoredState(this)
        allChronometers.addAll(chronometersStoredState.restoreState())

        // Initializes the chronometer, buttons and progressbar
        chronometerView = findViewById(R.id.mainChronometer)
        mainStartPauseButton = findViewById(R.id.mainStartPauseButton)
        resetButton = findViewById(R.id.resetButton)
        mainProgressBar = findViewById(R.id.mainProgressBar)
        editMainText = findViewById(R.id.editMainText)

        mainChronometerViews = MainChronometerViews(chronometerView, editMainText, mainProgressBar, mainStartPauseButton)

        val mainChronometerStrings = MainChronometerStrings(getString(R.string.start), getString(R.string.pause))

        recyclerView = findViewById(R.id.recyclerView)
        floatingActionButton = findViewById(R.id.floatingActionButton)

        // Initializes the ChronometerState and the mainChronometer objects
        mainChronometer = MainChronometer(mainChronometerStrings, resetButton, mainChronometerViews )
        secondaryChronometers = SecondaryChronometers(allChronometers, recyclerView, mainChronometerViews, mainChronometer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChronometerAdapter(secondaryChronometers)

        // Sets the listeners
        mainChronometer.setStartPauseButtonClickListener()
        mainChronometer.setResetButtonClickListener()
        mainChronometer.setChronometerTickListener()


        floatingActionButton.setOnClickListener {
            // Adds a new chronometer to the list
            secondaryChronometers.addChronometer(Triple(0, false, ""))
        }
    }

    override fun onPause() {
        super.onPause()
        (recyclerView.adapter as ChronometerAdapter).saveElapsedTime(recyclerView)

        // Saves the state to SharedPreferences
        chronometersStoredState.saveState(allChronometers)
    }
}