package com.followapp.mytime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.followapp.mytime.fragments.ClockFragment
import com.followapp.mytime.fragments.TimeTrackerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    private lateinit var storedState: StoredState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Is not using the id but the layout name

        supportActionBar?.hide()   // To remove the ActionBar

        // Restores the state from SharedPreferences
        storedState = StoredState(this)

        val clockFragment = ClockFragment() // links the val to the class ClockFragment
        val timeTrackerFragment = TimeTrackerFragment(storedState)  // links the val to the class TimeTrackerFragment

        setCurrentFragment(timeTrackerFragment)  // Selects the fragment to be shown

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)  // finds the bottomNavigationView in the activity_main by its id
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_clock->setCurrentFragment(clockFragment)  // item id selected the in bottom_nav_menu.xml, sets this fragment as current
                R.id.navigation_time_tracker->setCurrentFragment(timeTrackerFragment)  // item id selected the in bottom_nav_menu.xml, sets this fragment as current
            }
            true
        }
    }

    // This function replaces the fragment in activity_main.xml by the one indicated by params
    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)  // this name is the given to the FrameLayout in activity_main.xml
            commit()
        }

}