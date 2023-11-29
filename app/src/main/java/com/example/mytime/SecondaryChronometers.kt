package com.example.mytime

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import com.example.mytime.dataClasses.MainChronometerViews

class SecondaryChronometers(
    private val allChronometers: MutableList<Triple<Long, Boolean, String>>,
    private var recyclerView: RecyclerView,
    mainChronometerViews: MainChronometerViews,
    private val mainChronometer: MainChronometer
) {
    private val chronometersStoredState = ChronometersStoredState(recyclerView.context)
    private val chronometerView = mainChronometerViews.chronometerView
    private val editMainText = mainChronometerViews.editMainText
//    private val mainProgressBar = mainChronometerViews.mainProgressBar
//    private val mainStartPauseButton = mainChronometerViews.mainStartPauseButton

    private val stoppedChronometers = allChronometers.drop(1).toMutableList()

    fun getSize(): Int {
        return stoppedChronometers.size
    }

    fun getIterable(): Iterable<IndexedValue<Triple<Long, Boolean, String>>>{
        return stoppedChronometers.withIndex()
    }

    fun addChronometer(chronometer: Triple<Long, Boolean, String>) {
        stoppedChronometers.add(chronometer)

        // Notifies the adapter
        recyclerView.adapter?.notifyItemInserted(getSize())
        recyclerView.adapter?.notifyItemRangeChanged(getSize()-1, getSize())


        // Updates the sate
        allChronometers.add(chronometer)
        chronometersStoredState.saveState(allChronometers)
    }

    fun remove(position: Int) {
        stoppedChronometers.removeAt(position)

        // Notifies the adapter
        recyclerView.adapter?.notifyItemRemoved(position)
        recyclerView.adapter?.notifyItemRangeChanged(position, getSize())

        // Updates the sate
        allChronometers.removeAt(position+1)
        chronometersStoredState.saveState(allChronometers)
    }

    fun get(position: Int): Triple<Long, Boolean, String> {
        return stoppedChronometers[position]
    }

    fun update(chronometer: Triple<Long, Boolean, String>, position: Int) {
        stoppedChronometers[position] = chronometer
        allChronometers[position+1] = chronometer
        chronometersStoredState.saveState(allChronometers)
    }

    fun playAndConvertToMain(position: Int) {
        val newMainChronometer = stoppedChronometers.removeAt(position)
        val (elapsedTime, _, text) =  newMainChronometer
        val oldMainChronometer: Triple<Long, Boolean, String> = Triple(SystemClock.elapsedRealtime() - chronometerView.base, false, editMainText.text.toString())

        // Updates the main chronometer
        editMainText.setText(text)
        chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime
        mainChronometer.startMainChronometer()

        // Notifies the adapter
        recyclerView.adapter?.notifyItemRemoved(position)
        recyclerView.adapter?.notifyItemRangeChanged(position, getSize())

        // Adds the old main chronometer to the secondary list
        stoppedChronometers.add(0, oldMainChronometer)
        recyclerView.adapter?.notifyItemInserted(0)
        recyclerView.adapter?.notifyItemRangeChanged(0, getSize())
        recyclerView.scrollToPosition(0)

//        startStopButton.setImageResource(R.drawable.ic_pause) // Set the pause icon
//        startStopButton.contentDescription = holder.view.context.getString(R.string.pause)

        // Updates the sate
        allChronometers.removeAt(position+1)
        allChronometers.add(0, newMainChronometer)
        chronometersStoredState.saveState(allChronometers)
    }
}