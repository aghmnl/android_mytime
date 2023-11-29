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

class ChronometerAdapter(
        private val secondaryChronometers: SecondaryChronometers
    ) : RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {

    class ChronometerViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun getItemCount() = secondaryChronometers.getSize()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChronometerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chronometer_row, parent, false)
        return ChronometerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChronometerViewHolder, position: Int) {
        val chronometer = holder.view.findViewById<Chronometer>(R.id.mainChronometer)
        val startStopButton = holder.view.findViewById<ImageButton>(R.id.startStopButton)
        val editText = holder.view.findViewById<EditText>(R.id.editText)
        val removeButton = holder.view.findViewById<ImageButton>(R.id.removeButton)

        // Restore the state
        val (elapsedTime, isCounting, text) = secondaryChronometers.get(position)
        chronometer.base = SystemClock.elapsedRealtime() - elapsedTime

        val handler = Handler(Looper.getMainLooper())

        // TODO: the initialization should take into account the main chronometer state
        editText.setText(text)

        startStopButton.setOnClickListener {
            // TODO: the secondary chronometers cannot be counting, only the main chronometer can
            secondaryChronometers.playAndConvertToMain(position)
        }

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // It takes the new text as a parameter and saves it in the state
                val runnable = Runnable {
                    secondaryChronometers.update(Triple(SystemClock.elapsedRealtime() - chronometer.base, isCounting, s.toString()), holder.adapterPosition)
                }

                handler.removeCallbacksAndMessages(null) // Cancel the previous delay
                handler.postDelayed(runnable, 300) // Start a new delay
            }

            override fun afterTextChanged(s: Editable) {}
        })

        removeButton.setOnClickListener {
            secondaryChronometers.remove(position)
        }
    }

    fun saveElapsedTime(recyclerView: RecyclerView) {
        for ((index, chronometer) in secondaryChronometers.getIterable()) {
            val (_, isCounting, text) = chronometer
            if (isCounting) {
                // If the chronometer is counting, update the elapsed time
                val view = recyclerView.findViewHolderForAdapterPosition(index)?.itemView
                val chronometerView = view?.findViewById<Chronometer>(R.id.mainChronometer)
                val newElapsedTime = SystemClock.elapsedRealtime() - chronometerView?.base!!
                secondaryChronometers.update(Triple(newElapsedTime, true, text), index)
            }
        }
    }
}