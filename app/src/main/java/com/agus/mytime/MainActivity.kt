package com.agus.mytime

import android.os.Bundle
import android.widget.Chronometer as ChronometerWidget
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agus.mytime.dataClasses.MainChronometerStrings
import com.agus.mytime.dataClasses.MainChronometerViews
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.agus.mytime.dataClasses.Chronometer
import com.example.mytime.R

class MainActivity : AppCompatActivity() {

    // Code moved to other classes for better understanding
    private lateinit var chronometersStoredState: ChronometersStoredState
    private lateinit var mainChronometer: MainChronometer
    private lateinit var secondaryChronometers: SecondaryChronometers
    // Declare variables for the chronometer, buttons and progressbar
    private lateinit var chronometerView: ChronometerWidget
    private lateinit var editMainText: EditText
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainStartPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var removeButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    // Creates the list of chronometers
    private val allChronometers = mutableListOf<Chronometer>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To remove the ActionBar
        supportActionBar?.hide()

        // Restores the state from SharedPreferences
        chronometersStoredState = ChronometersStoredState(this)
        allChronometers.addAll(chronometersStoredState.restoreState())

        // Initializes the chronometer, buttons and progressbar
        chronometerView = findViewById(R.id.mainChronometer)
        mainStartPauseButton = findViewById(R.id.mainStartPauseButton)
        resetButton = findViewById(R.id.resetButton)
        removeButton = findViewById(R.id.removeMainButton)
        mainProgressBar = findViewById(R.id.mainProgressBar)
        editMainText = findViewById(R.id.editMainText)
        floatingActionButton = findViewById(R.id.floatingActionButton)

        val mainChronometerViews = MainChronometerViews(chronometerView, editMainText, mainProgressBar, mainStartPauseButton, resetButton, removeButton)
        val mainChronometerStrings = MainChronometerStrings(getString(R.string.start), getString(R.string.pause))

        recyclerView = findViewById(R.id.recyclerView)

        // Initializes the main and secondary chronometers
        mainChronometer = MainChronometer(allChronometers, mainChronometerStrings, mainChronometerViews)
        mainChronometer.initialize()
        secondaryChronometers = SecondaryChronometers(allChronometers, recyclerView, mainChronometer)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChronometerAdapter(secondaryChronometers)

        // Sets the listeners
        mainChronometer.setStartPauseButtonClickListener()
        mainChronometer.setResetButtonClickListener()
        mainChronometer.setChronometerTickListener()
        mainChronometer.setRemoveButtonClickListener()

        floatingActionButton.setOnClickListener {
            // Adds a new chronometer to the list
            secondaryChronometers.addChronometer(Chronometer(0, false, ""))
        }
    }

    override fun onPause() {
        super.onPause()
        // Saves the state to SharedPreferences
        chronometersStoredState.saveState(allChronometers)
    }
}