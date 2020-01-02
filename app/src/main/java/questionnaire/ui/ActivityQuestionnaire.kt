package questionnaire.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import application.R
import application.Helper
import questionnaire.Questionnaire
import java.lang.Exception

class ActivityQuestionnaire : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)
        try {

            val path = intent.getStringExtra("path")
            val json = Helper.converting(assets.open(path))
            val questionnaire = Questionnaire.createQuestionnaire(json)

            val fragment = PresentativeQuestionnaire()
            fragment.activity = this
            fragment.questionnaire = questionnaire!!

            supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
        }
        catch (ex: Exception){
            Toast.makeText(baseContext, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
