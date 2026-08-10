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
import com.followapp.mytime.Utilities.Companion.createAlertDialog
import com.followapp.mytime.dataClasses.Chronometer

class ChronometerAdapter(
    private val secondaryChronometers: SecondaryChronometers
) : RecyclerView.Adapter<ChronometerAdapter.ChronometerViewHolder>() {

    class ChronometerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = secondaryChronometers.getSize()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChronometerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.chronometer_row, parent, false)
        return ChronometerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChronometerViewHolder, position: Int) {
        val mainChronometer = holder.view.findViewById<ChronometerWidget>(R.id.mainChronometer)
        val startStopButton = holder.view.findViewById<ImageButton>(R.id.startStopButton)
        val editText = holder.view.findViewById<EditText>(R.id.editText)
        val removeButton = holder.view.findViewById<ImageButton>(R.id.removeButton)

        // Restore the state
        val (elapsedTime, isCounting, text) = secondaryChronometers.get(position)
        mainChronometer.base = SystemClock.elapsedRealtime() - elapsedTime

        val handler = Handler(Looper.getMainLooper())

        if (text !== "") {
            editText.setText(text)
        } else {
            val projectString = holder.view.context.getString(R.string.project_number, position + 1)
            editText.setText(projectString)
        }
        // Set a unique content description for each EditText and each button
        startStopButton.contentDescription = "Start/Stop for row $position"
        removeButton.contentDescription = "Remove for row $position"

        startStopButton.setOnClickListener {
            secondaryChronometers.startAndConvertToMain(position)
        }

        // This is to perform after the text was edited
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            // When an EditText loses focus, it triggers the onTextChanged method. This can happen when an item is removed or added from/to the RecyclerView
            // This was generating an IndexOutOfBoundsException because it was sending a -1 position to the secondaryChronometers.update
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                // This checks that the function was not triggered because of removal or addition of rows.
                if (editText.hasFocus()) {
                    // It takes the new text as a parameter and saves it in the state
                    val runnable = Runnable {
                        val adapterPosition = holder.adapterPosition
                        secondaryChronometers.update(
                            Chronometer(SystemClock.elapsedRealtime() - mainChronometer.base, isCounting, s.toString()),
                            adapterPosition
                        )
                    }

                    handler.removeCallbacksAndMessages(null) // Cancel the previous delay
                    handler.postDelayed(runnable, 300) // Start a new delay
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        removeButton.setOnClickListener {
            val context = holder.view.context
            createAlertDialog(
                context, context.getString(R.string.confirmation_remove_chronometer), context.getString(R.string.ok), context.getString(R.string.cancel)
            ) {
                secondaryChronometers.remove(position)
            }
        }
    }
}
