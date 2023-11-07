package com.example.mytime

import android.os.SystemClock
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

val Chronometer.isCounting: Boolean
    get() = base <= SystemClock.elapsedRealtime()

class ChronometerAdapter(private val chronometers: List<Pair<Chronometer, ImageButton>>) :
    RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {

    class ChronometerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChronometerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chronometer_row, parent, false)
        return ChronometerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChronometerViewHolder, position: Int) {
        val chronometer = holder.view.findViewById<Chronometer>(R.id.chronometer)
        val startStopButton = holder.view.findViewById<ImageButton>(R.id.startStopButton)

        chronometer.base = chronometers[position].first.base

        chronometer.setOnChronometerTickListener {
            // Update the chronometer's display
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            chronometer.text = DateUtils.formatElapsedTime(elapsedMillis / 1000)
        }

        startStopButton.setOnClickListener {
            if (chronometer.isCounting) {
                chronometer.stop()
                startStopButton.contentDescription = holder.view.context.getString(R.string.start)
                startStopButton.setImageResource(R.drawable.ic_play) // Set the play icon
            } else {
                chronometer.start()
                startStopButton.contentDescription = holder.view.context.getString(R.string.stop)
                startStopButton.setImageResource(R.drawable.ic_stop) // Set the stop icon
            }
        }
    }

    override fun getItemCount() = chronometers.size
}