package questionnaire.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import questionnaire.Settings


fun openQuestionnaire(activity: AppCompatActivity, settings: Settings){
    val intent = Intent(activity.baseContext, ActivityQuestionnaire::class.java)
    intent.putExtra("path", settings.path)
    activity.startActivity(intent)
}