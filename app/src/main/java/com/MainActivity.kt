package com

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.application.R
import com.fragments.GameOfflineSessions
import com.fragments.GameOnlineSessions
import com.fragments.HomeSettings
import com.users.User


class MainActivity : AppCompatActivity() {

    lateinit var user: User
    lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            try{
                switchFragment(it.itemId)
                true
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                false
            }
        }

        downloadUser()
        switchFragment(R.id.navigation_home)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 0 && resultCode == RESULT_OK){

            val obResult = data.getStringExtra("obResult")
            if (obResult != null){

            }
            Log.e("obResult", obResult.toString())
        }
        Log.e("result", data.toString())
    }

    private fun downloadUser(){
        // загрузка пользователя производится из assets

        val inputStream = assets.open("user.json")
        val json = Helper.converting(inputStream)

        val user = User.createUser(json)
        if (user != null)
            this.user = user

        Log.e("user", this.user.toJsonObject())
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
