package com.followapp.mytime.timeTracker

import android.content.Context
import android.os.SystemClock
import com.followapp.mytime.R
import com.followapp.mytime.dataClasses.Chronometer


class ChronometersStoredState(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)

    // Saves the state in the sharedPreferences receiving a list of all the chronometers as parameter
    fun saveState(chronometers: List<Chronometer>) {
        val editor = sharedPreferences.edit()
        editor.putInt("size", chronometers.size)
        for (i in chronometers.indices) {
            editor.putLong("base_$i", chronometers[i].elapsedTime)
            editor.putBoolean("isCounting_$i", false)
            editor.putString("text_$i", chronometers[i].label)
        }
        editor.apply()
    }

    fun restoreState(): MutableList<Chronometer> {
        val chronometers = mutableListOf<Chronometer>()
        val size = sharedPreferences.getInt("size", 0)
        if (size > 0) {
            for (i in 0 until size) {
                val base = sharedPreferences.getLong("base_$i", SystemClock.elapsedRealtime())
                val isCounting = sharedPreferences.getBoolean("isCounting_$i", false)
                val text = sharedPreferences.getString("text_$i", "")
                chronometers.add(Chronometer(base, isCounting, text ?: ""))
            }
        } else {
            chronometers.add(Chronometer(0, false, context.getString(R.string.new_project)))
        }
        return chronometers
    }
}