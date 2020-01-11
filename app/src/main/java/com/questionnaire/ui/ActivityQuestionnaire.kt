package com.questionnaire.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.application.R
import com.application.Helper
import com.application.MainActivity
import com.questionnaire.Questionnaire
import com.questionnaire.Settings
import java.lang.Exception


class ActivityQuestionnaire : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        if (android.os.Build.VERSION.SDK_INT >= 21){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#ACACAC")
        }

        try {
            var json = intent.getStringExtra("settings")
            val settings = Settings.createSettings(json)
            Log.e("json", json)
            json = Helper.converting(assets.open(settings.path))
            val questionnaire = Questionnaire.createQuestionnaire(json)

            if (questionnaire is Questionnaire){
                questionnaire.settings = settings
                val fragment = PresentativeQuestionnaire()
                fragment.activity = this
                fragment.questionnaire = questionnaire

                supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            }
            else {
                startActivity(Intent(baseContext, MainActivity::class.java))
                Toast.makeText(baseContext, "не получилось загрузить анкету", Toast.LENGTH_SHORT).show()
            }
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
            Toast.makeText(baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
