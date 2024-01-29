package com.followapp.mytime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class OLDMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Is not using the id but the layout name

        supportActionBar?.hide()   // To remove the ActionBar

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView) // As defined in activity_main.xml
        val navController = findNavController(R.id.flFragment)  // As defined in activity_main.xml
        navView.setupWithNavController(navController)
    }
}