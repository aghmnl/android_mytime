package com.agus.mytime

import android.content.Context
import android.widget.Toast
import com.example.mytime.R

class Utilities(private val context: Context) {
    private fun targetAchieved() {
        Toast.makeText(context, context.getString(R.string.times_up), Toast.LENGTH_SHORT).show()
    }
}