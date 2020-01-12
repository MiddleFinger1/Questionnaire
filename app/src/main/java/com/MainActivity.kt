package com

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.application.R
import com.fragments.GameOfflineSessions
import com.fragments.GameOnlineSessions
import com.fragments.HomeSettings


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment? = when (item.itemId) {
            R.id.navigation_home ->
                HomeSettings()
            R.id.navigation_dashboard ->
                GameOfflineSessions().apply { activity = this@MainActivity}
            R.id.navigation_notifications -> {
                GameOnlineSessions()
            }
            else -> null
        }
        if (fragment != null)
            supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (android.os.Build.VERSION.SDK_INT >= 21){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#ACACAC")
        }
    }
}
