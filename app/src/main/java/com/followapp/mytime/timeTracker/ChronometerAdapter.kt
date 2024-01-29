package com.followapp.mytime.timeTracker

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer as ChronometerWidget
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.followapp.mytime.R
import com.followapp.mytime.dataClasses.Chronometer

class ChronometerAdapter(
        private val stoppedChronometers: SecondaryChronometers
    ) : RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {

    class ChronometerViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun getItemCount() = stoppedChronometers.getSize()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChronometerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chronometer_row, parent, false)
        return ChronometerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChronometerViewHolder, position: Int) {
        val chronometer = holder.view.findViewById<ChronometerWidget>(R.id.mainChronometer)
        val startStopButton = holder.view.findViewById<ImageButton>(R.id.startStopButton)
        val editText = holder.view.findViewById<EditText>(R.id.editText)
        val removeButton = holder.view.findViewById<ImageButton>(R.id.removeButton)

        // Restore the state
        val (elapsedTime, isCounting, text) = stoppedChronometers.get(position)
        chronometer.base = SystemClock.elapsedRealtime() - elapsedTime

        val handler = Handler(Looper.getMainLooper())

        if (text !== "" ) {
            editText.setText(text)
        } else {
            val projectString = holder.view.context.getString(R.string.project_number, position+1)
            editText.setText(projectString)
        }
        // Set a unique content description for each EditText and each button
        startStopButton.contentDescription = "Start/Stop for row $position"
        removeButton.contentDescription = "Remove for row $position"

        startStopButton.setOnClickListener {
            stoppedChronometers.startAndConvertToMain(position)
        }

        editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // It takes the new text as a parameter and saves it in the state
                val runnable = Runnable {
                    stoppedChronometers.update(Chronometer(SystemClock.elapsedRealtime() - chronometer.base, isCounting, s.toString()), holder.adapterPosition)
                }

                handler.removeCallbacksAndMessages(null) // Cancel the previous delay
                handler.postDelayed(runnable, 300) // Start a new delay
            }

            override fun afterTextChanged(s: Editable) {}
        })

        removeButton.setOnClickListener {
            stoppedChronometers.remove(position)
        }
    }
}