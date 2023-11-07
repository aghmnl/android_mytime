package com.example.mytime

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class ChronometerAdapter(private val chronometers: List<Pair<Chronometer, ImageButton>>) :
    RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {
    class ChronometerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = chronometers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChronometerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chronometer_row, parent, false)
        return ChronometerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChronometerViewHolder, position: Int) {
        val chronometer = holder.view.findViewById<Chronometer>(R.id.chronometer)
        val startStopButton = holder.view.findViewById<ImageButton>(R.id.startStopButton)

        // Declare and initialize variables for the start state and elapsed time
        var start = true
        var elapsedTime = 0L

        startStopButton.setOnClickListener {
            if (start) {
                // If the start variable is true, start the chronometer
                println("Starting $position")
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                chronometer.start()
                start = false
                startStopButton.setImageResource(R.drawable.ic_pause) // Set the pause icon
                startStopButton.contentDescription = holder.view.context.getString(R.string.pause)
            } else {
                // If the start variable is false, stop the chronometer and save the elapsed time
                println("Stopping $position")
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                start = true
                startStopButton.setImageResource(R.drawable.ic_play) // Set the play icon
                startStopButton.contentDescription = holder.view.context.getString(R.string.start)
            }
        }
    }

}