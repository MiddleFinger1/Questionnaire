package com.ui

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.R
import com.logic.IOManager.constructorFileName
import com.logic.IOManager.user
import com.logic.IOManager.userFileName
import com.json.user.ObResult
import com.logic.IOManager


class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            switchFragment(it.itemId)
            true
        }

        IOManager.downloadUser(this)
        if (intent != null) {
            val type: String? = intent.getStringExtra("type")
            if (type != null) {
                Log.e("typeIntent", type)
                when (type) {
                    "constructor" -> {
                        val json: String? = intent.getStringExtra("questionnaire")
                        if (json != null){
                            IOManager.writeFile(constructorFileName, json)

                        }
                    }
                    "obResult" -> {
                        val json = intent.getStringExtra("obResult")
                        if (json != null) {
                            val obResult = ObResult.createObResult(json)
                            if (obResult != null){
                                val index = user.analytics.indexOf(obResult)
                                if (index >= 0)
                                    user.analytics[index] = obResult
                                else user.analytics.add(obResult)
                            }
                            IOManager.writeFile(userFileName, user.toJsonObject())
                        }

                    }
                }
            }
        }
        switchFragment(R.id.navigation_home)
    }

    private fun switchFragment(id: Int){
        val item = navView.menu.findItem(id)
        item.isChecked = true
        val fragment: Fragment? = when (item.itemId) {
            R.id.navigation_home ->
                HomeSettings().apply { activity = this@MainActivity}
            R.id.navigation_dashboard ->
                GameOfflineSessions().apply { activity = this@MainActivity}
            R.id.navigation_notifications -> {
                GameOnlineSessions()
            }
            else -> null
        }
        if (fragment != null)
            supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
    }
}
