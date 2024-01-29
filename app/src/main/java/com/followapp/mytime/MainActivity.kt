package com.followapp.mytime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Is not using the id but the layout name

        supportActionBar?.hide()   // To remove the ActionBar

        val firstFragment = FirstFragment() // links the val to the class FirstFragment
        val secondFragment = SecondFragment()  // links the val to the class SecondFragment

        setCurrentFragment(firstFragment)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)  // finds the bottomNavigationView in the activity_main by its id
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_clock->setCurrentFragment(firstFragment)  // item id the in bottom_nav_menu.xml, sets a specific fragment as current
                R.id.navigation_time_tracker->setCurrentFragment(secondFragment)  // item id in bottom_nav_menu.xml, sets a specific fragment as current
            }
            true
        }

    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)  // this name is the given to the FrameLayout in activity_main.xml
            commit()
        }

}