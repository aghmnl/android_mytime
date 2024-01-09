package com.agus.mytime.dataClasses

import android.widget.Chronometer
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar

data class MainChronometerViews(
    val chronometerView: Chronometer,
    val editMainText: EditText,
    val mainProgressBar: ProgressBar,
    val mainStartPauseButton: ImageButton
)
