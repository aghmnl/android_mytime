package com.example.mytime

import android.content.Context
import android.os.SystemClock

class ChronometerState(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)

    fun saveState(chronometers: List<Triple<Long, Boolean, String>>) {
        val editor = sharedPreferences.edit()
        editor.putInt("size", chronometers.size)
        for (i in chronometers.indices) {
            editor.putLong("base_$i", chronometers[i].first)
            editor.putBoolean("isCounting_$i", chronometers[i].second)
            editor.putString("text_$i", chronometers[i].third)
        }
        editor.apply()
    }

    fun restoreState(): MutableList<Triple<Long, Boolean, String>> {
        val chronometers = mutableListOf<Triple<Long, Boolean, String>>()
        val size = sharedPreferences.getInt("size", 0)
        for (i in 0 until size) {
            val base = sharedPreferences.getLong("base_$i", SystemClock.elapsedRealtime())
            val isCounting = sharedPreferences.getBoolean("isCounting_$i", false)
            val text = sharedPreferences.getString("text_$i", "")
            chronometers.add(Triple(base, isCounting, text ?: ""))
        }
        return chronometers
    }
}