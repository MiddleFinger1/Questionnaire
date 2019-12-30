package questionnaire.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import application.R
import application.Helper
import questionnaire.Questionnaire

class ActivityQuestionnaire : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questionnaire)

        val fragment = PresentativeQuestionnaire()
        fragment.activity = this
        fragment.questionnaire = Questionnaire.createQuestionnaire(Helper.converting(resources.openRawResource(R.raw.json)))!!

        supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
    }

}
