package com.followapp.mytime.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
//import android.view.LayoutInflater
import android.view.View
//import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.followapp.mytime.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ClockFragment : Fragment(R.layout.fragment_clock) {
// This takes the layout fragment_first and inflates it

    private lateinit var textView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            textView.text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            handler.postDelayed(this, 1000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView = view.findViewById(R.id.clock)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}