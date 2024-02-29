package com.followapp.mytime

import com.followapp.mytime.dataClasses.Chronometer

//import android.content.Context
//import android.widget.Toast

class Utilities {
//class Utilities(private val context: Context) {
//    private fun targetAchieved() {
//        Toast.makeText(context, context.getString(R.string.times_up), Toast.LENGTH_SHORT).show()
//    }

    // This is used just for debugging purposes
    companion object {
        fun printAll(chronometers: MutableList<Chronometer>, message: String) {
            println(message)
            chronometers.forEachIndexed { index, chron -> print("$index "); chron.print() }
        }
    }
}
