package com.ui.start

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.R
import com.json.user.User
import com.logic.IOManager
import com.ui.MainActivity


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        // проверяем, есть ли файлы приложения
        val isFiles = IOManager.findFilesConfigs()
        //
        val fragment: Fragment =
            if (isFiles)
                BackFragment()
            else AuthFragment()

        Log.e("state", IOManager.findFilesConfigs().toString())

        supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
    }

    fun enterToAccount(user: User){
        val intent = Intent(baseContext, MainActivity::class.java)
        IOManager.user = user
        startActivity(intent)
    }
}
