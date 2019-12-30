package application

import android.content.Intent
import android.os.Bundle
import application.fragments.GameOfflineSessions
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import questionnaire.ui.ActivityQuestionnaire


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment? = when (item.itemId) {
            R.id.navigation_home -> null
            R.id.navigation_dashboard ->
                GameOfflineSessions()
            R.id.navigation_notifications -> {
                startActivity(Intent(baseContext, ActivityQuestionnaire::class.java))
                null
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
    }
}
