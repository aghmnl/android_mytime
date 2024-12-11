package com.followapp.mytime.dataClasses

data class Chronometer(var elapsedTime: Long, var isCounting: Boolean, val label: String) {
    fun setTime(newElapsedTime: Long) {
        elapsedTime = newElapsedTime
    }

    fun setIsCounting(newIsCounting: Boolean) {
        isCounting = newIsCounting
    }

    // This is used just for debugging purposes
    fun print() {
        println("Elapsed time: $elapsedTime, ${if (isCounting) "started" else "stopped"}, $label")
    }


}