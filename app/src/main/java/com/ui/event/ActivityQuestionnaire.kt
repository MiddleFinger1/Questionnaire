package com.ui.event

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.ui.MainActivity
import com.R
import com.json.questionnaire.Questionnaire
import com.json.user.ObResult
import java.lang.Exception


class ActivityQuestionnaire : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        try {
            val json = intent.getStringExtra("questionnaire")
            Log.e("json", json)
            val questionnaire = Questionnaire.createQuestionnaire(json)
            val obResultJson: String? = intent.getStringExtra("obResult")
            Log.e("laterObResult", obResultJson.toString())
            val obResult = ObResult.createObResult(obResultJson.toString())
            if (questionnaire is Questionnaire){
                val fragment = PresentativeQuestionnaire()
                fragment.activity = this
                fragment.questionnaire = questionnaire
                fragment.laterObResult = obResult
                supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            }
            else throw Exception()
        }
        catch (ex: Exception){
            startActivity(Intent(baseContext, MainActivity::class.java))
            Toast.makeText(baseContext, "не получилось загрузить анкету", Toast.LENGTH_SHORT).show()
            Log.e("ex", ex.toString())
        }
    }

}
