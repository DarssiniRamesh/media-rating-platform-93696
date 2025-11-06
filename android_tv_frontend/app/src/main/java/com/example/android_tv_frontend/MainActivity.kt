package com.example.android_tv_frontend

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

/**
 * PUBLIC_INTERFACE
 * Main Activity for Android TV.
 *
 * This hosts the NavHostFragment which displays the Browse screen and navigates to Details.
 * Ocean Professional theme is applied via styles.xml.
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ensure NavHostFragment is attached.
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                ?: return
        navHostFragment.navController // touch to init
    }

    // PUBLIC_INTERFACE
    fun navController() = findNavController(R.id.nav_host_fragment)
}
