package com.agus.mytime.dataClasses

data class Chronometer(var elapsedTime: Long, var isCounting: Boolean, val label: String) {
    fun setTime(newElapsedTime: Long) {
        elapsedTime = newElapsedTime
    }

    fun setIsCounting(newIsCounting: Boolean) {
        isCounting = newIsCounting
    }

}