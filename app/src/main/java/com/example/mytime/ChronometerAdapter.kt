package com.example.mytime

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class ChronometerAdapter(private val chronometers: MutableList<Triple<Long, Boolean, String>>) :
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
        val editText = holder.view.findViewById<EditText>(R.id.editText)

        // Restore the state
        var (elapsedTime, isCounting, text) = chronometers[position]
        chronometer.base = SystemClock.elapsedRealtime() - elapsedTime

        // Depending on the isCounting state, initializes the chronometers
        editText.setText(text)
        if (isCounting) {
            chronometer.start()
            startStopButton.setImageResource(R.drawable.ic_pause) // Set the pause icon
            startStopButton.contentDescription = holder.view.context.getString(R.string.pause)
        } else {
            chronometer.stop()
            startStopButton.setImageResource(R.drawable.ic_play) // Set the play icon
            startStopButton.contentDescription = holder.view.context.getString(R.string.start)
        }

        startStopButton.setOnClickListener {
            if (isCounting) {
                // Stops and saves the elapsed time
                elapsedTime = SystemClock.elapsedRealtime() - chronometer.base
                chronometer.stop()
                isCounting = false
                startStopButton.setImageResource(R.drawable.ic_play) // Set the play icon
                startStopButton.contentDescription = holder.view.context.getString(R.string.start)
            } else {
                // Starts from the elapsed time
                chronometer.base = SystemClock.elapsedRealtime() - elapsedTime
                chronometer.start()
                isCounting = true
                startStopButton.setImageResource(R.drawable.ic_pause) // Set the pause icon
                startStopButton.contentDescription = holder.view.context.getString(R.string.pause)
            }
            // Save the state
            chronometers[position] = Triple(elapsedTime, isCounting, editText.text.toString())
//            println("ElapsedTime $elapsedTime, isCounting $isCounting, editText ${editText.text} position $position")
        }
    }
}