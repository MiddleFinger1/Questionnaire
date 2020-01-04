package com.questionnaire.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.questionnaire.Settings


fun openQuestionnaire(activity: AppCompatActivity, settings: Settings){
    val intent = Intent(activity.baseContext, ActivityQuestionnaire::class.java)
    intent.putExtra("settings", settings.toJsonObject())
    activity.startActivity(intent)
}