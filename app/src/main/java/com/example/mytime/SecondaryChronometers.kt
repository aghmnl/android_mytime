package com.example.mytime

import androidx.recyclerview.widget.RecyclerView
import com.example.mytime.dataClasses.Chronometer

class SecondaryChronometers(
    private val allChronometers: MutableList<Chronometer>,
    private var recyclerView: RecyclerView,
//    mainChronometerViews: MainChronometerViews,
    private val mainChronometer: MainChronometer
) {
    private val chronometersStoredState = ChronometersStoredState(recyclerView.context)
//    private val chronometerView = mainChronometerViews.chronometerView
//    private val editMainText = mainChronometerViews.editMainText
//    private val mainProgressBar = mainChronometerViews.mainProgressBar
//    private val mainStartPauseButton = mainChronometerViews.mainStartPauseButton

    private val stoppedChronometers = allChronometers.drop(1).toMutableList()

    fun getSize(): Int {
        return stoppedChronometers.size
    }

//    fun getIterable(): Iterable<IndexedValue<Chronometer>>{
//        return stoppedChronometers.withIndex()
//    }

    fun addChronometer(chronometer: Chronometer) {
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

    fun get(position: Int): Chronometer {
        return stoppedChronometers[position]
    }

    fun update(chronometer: Chronometer, position: Int) {
        stoppedChronometers[position] = chronometer
        allChronometers[position+1] = chronometer
        chronometersStoredState.saveState(allChronometers)
    }

    fun startAndConvertToMain(position: Int) {
        // This stops the main chronometer when converting the secondary chronometer into a main one
        if(allChronometers[0].isCounting) mainChronometer.stop()

        val newMainChronometer = stoppedChronometers.removeAt(position)
//        val (elapsedTime, _, text) =  newMainChronometer

        val oldMainChronometer = allChronometers[0]

        // Updates the main chronometer
//        editMainText.setText(text)
//        chronometerView.base = SystemClock.elapsedRealtime() - elapsedTime


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

        mainChronometer.initialize()
        mainChronometer.start()

        chronometersStoredState.saveState(allChronometers)
    }
}