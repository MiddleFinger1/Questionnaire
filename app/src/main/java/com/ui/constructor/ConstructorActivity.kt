package com.ui.constructor

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.R
import com.json.questionnaire.Questionnaire
import com.json.questionnaire.Settings


class ConstructorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constructor)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        val json: String? = intent.getStringExtra("questionnaire")
        val questionnaire = Questionnaire.createQuestionnaire(json.toString())
        val fragment = ConstructorQuestionnaire()
            fragment.questionnaire = questionnaire ?: Questionnaire(Settings(""))
            fragment.activity = this
        supportFragmentManager.beginTransaction().replace(R.id.MainConstructor, fragment).commit()
    }

}
