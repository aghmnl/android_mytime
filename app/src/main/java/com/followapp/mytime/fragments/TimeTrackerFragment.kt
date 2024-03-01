package com.followapp.mytime.fragments

import android.os.Bundle
import android.view.View
import android.widget.Chronometer as ChronometerWidget
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytime.timeTracker.ChronometerAdapter
import com.followapp.mytime.timeTracker.MainChronometer
// I'm quite curious about this import
import com.followapp.mytime.R
import com.followapp.mytime.StoredState
import com.followapp.mytime.timeTracker.SecondaryChronometers
import com.followapp.mytime.dataClasses.MainChronometerStrings
import com.followapp.mytime.dataClasses.MainChronometerViews
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.followapp.mytime.dataClasses.Chronometer
import com.followapp.mytime.Utilities.Companion.hideKeyboard

//import com.followapp.mytime.dataClasses.ChronometerViewModel

class TimeTrackerFragment(private val storedState: StoredState) : Fragment(R.layout.fragment_time_tracker) {

    // Code moved to other classes for better understanding
    private lateinit var mainChronometer: MainChronometer
    private lateinit var secondaryChronometers: SecondaryChronometers
    // Declare variables for the chronometer, buttons and progressbar
    private lateinit var chronometerView: ChronometerWidget
    private lateinit var editMainText: EditText
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainStartPauseButton: ImageButton
    private lateinit var resetButton: ImageButton
    //    private lateinit var removeButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton

    // Creates the list of chronometers
    private val allChronometers = mutableListOf<Chronometer>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // a new instance of the TimeTrackerFragment is created each time one navigates to it.
        super.onViewCreated(view, savedInstanceState)

        // Restores the state from SharedPreferences
        if (allChronometers.isEmpty()) {
            allChronometers.addAll(storedState.restoreState())
        }
        // Initializes the chronometer, buttons and progressbar
        chronometerView = view.findViewById(R.id.mainChronometer)
        mainStartPauseButton = view.findViewById(R.id.mainStartPauseButton)
        resetButton = view.findViewById(R.id.resetButton)
        //        removeButton = findViewById(R.id.removeMainButton)
        mainProgressBar = view.findViewById(R.id.mainProgressBar)
        editMainText = view.findViewById(R.id.mainEditText)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)

        val mainChronometerViews = MainChronometerViews(chronometerView, editMainText, mainProgressBar, mainStartPauseButton, resetButton)
        //        val mainChronometerViews = MainChronometerViews(chronometerView, editMainText, mainProgressBar, mainStartPauseButton, resetButton, removeButton)
        val mainChronometerStrings = MainChronometerStrings(getString(R.string.start_stop), getString(
            R.string.pause
        ))

        recyclerView = view.findViewById(R.id.recyclerView)

        // Initializes the main and secondary chronometers
        mainChronometer = MainChronometer(requireContext(), allChronometers, mainChronometerStrings, mainChronometerViews)
        mainChronometer.initialize()
        secondaryChronometers = SecondaryChronometers(allChronometers, recyclerView, mainChronometer)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ChronometerAdapter(secondaryChronometers)

        // Sets the listeners
        mainChronometer.setStartPauseButtonClickListener()
        mainChronometer.setResetButtonClickListener()
        mainChronometer.setChronometerTickListener()
        //        mainChronometer.setRemoveButtonClickListener()


        floatingActionButton.setOnClickListener {
            // Adds a new chronometer to the list
            secondaryChronometers.addChronometer(Chronometer(0, false, ""))
        }
    }



    override fun onPause() {
        super.onPause()
        // Saves the state to SharedPreferences
        storedState.saveState(allChronometers)

        // TODO: It’s often better to handle the visibility of the keyboard in response to specific user actions or events,
        //  rather than hiding it every time the app is paused.
        // TODO: be aware that using static methods and passing around Activity can lead to memory leaks if not handled carefully.
        //  Always null-check and avoid keeping long-lived references to an Activity.
        // Apparently this was required to avoid a showing keyboard when opening the app. This behaviour couldn't be replicated.
        activity?.let { hideKeyboard(it) }
    }
}
