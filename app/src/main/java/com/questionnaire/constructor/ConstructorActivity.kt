package com.questionnaire.constructor

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.application.R

class ConstructorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constructor)

        window.setBackgroundDrawable(null)

        if (android.os.Build.VERSION.SDK_INT >= 21){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#ACACAC")
        }

        val fragment = ConstructorQuestionnaire()
        supportFragmentManager.beginTransaction().replace(R.id.MainConstructor, fragment).commit()
    }

}
