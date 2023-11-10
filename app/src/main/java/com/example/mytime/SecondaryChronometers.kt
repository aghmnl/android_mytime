package com.example.mytime

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SecondaryChronometers {
    fun setFloatingActionButtonClickListener(floatingActionButton: FloatingActionButton, recyclerView: RecyclerView, chronometers: MutableList<Triple<Long, Boolean, String>>) {
        // Set an onChronometerTickListener for the chronometer
        floatingActionButton.setOnClickListener {
            chronometers.add(Triple(SystemClock.elapsedRealtime(), false, ""))
            recyclerView.adapter?.notifyItemInserted(chronometers.size - 1)
        }
    }
}