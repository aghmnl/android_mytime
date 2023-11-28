package com.example.mytime

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class ChronometerAdapter(private val chronometers: MutableList<Triple<Long, Boolean, String>>) :
    RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {

    class ChronometerViewHolder(val view: View): RecyclerView.ViewHolder(view)

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
        val removeButton = holder.view.findViewById<ImageButton>(R.id.removeButton)

        // Restore the state
        var (elapsedTime, isCounting, text) = chronometers[position]
        chronometer.base = SystemClock.elapsedRealtime() - elapsedTime

        val handler = Handler(Looper.getMainLooper())

        // Depending on the isCounting state, initializes the chronometers
        editText.setText(text)

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
            // Updates the state
            chronometers[position] = Triple(elapsedTime, isCounting, editText.text.toString())
        }

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // It takes the new text as a parameter and saves it in the state
                val runnable = Runnable {
                    chronometers[holder.adapterPosition] = Triple(SystemClock.elapsedRealtime() - chronometer.base, isCounting, s.toString())
                }

                handler.removeCallbacksAndMessages(null) // Cancel the previous delay
                handler.postDelayed(runnable, 300) // Start a new delay
            }

            override fun afterTextChanged(s: Editable) {}
        })

        removeButton.setOnClickListener {
            // Removes the chronometer from the state
            chronometers.removeAt(position)
            // Notifies the adapter
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, chronometers.size)
        }
    }

    fun saveElapsedTime(recyclerView: RecyclerView) {
        for ((index, chronometer) in chronometers.withIndex()) {
            val (_, isCounting, text) = chronometer
            if (isCounting) {
                // If the chronometer is counting, update the elapsed time
                val view = recyclerView.findViewHolderForAdapterPosition(index)?.itemView
                val chronometerView = view?.findViewById<Chronometer>(R.id.chronometer)
                val newElapsedTime = SystemClock.elapsedRealtime() - chronometerView?.base!!
                chronometers[index] = Triple(newElapsedTime, true, text)
            }
        }
    }
}