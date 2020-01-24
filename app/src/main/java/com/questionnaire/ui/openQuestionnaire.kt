package com.questionnaire.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.questionnaire.Settings
import com.users.ObResult


fun openQuestionnaire(activity: AppCompatActivity, settings: Settings, obResult: ObResult? = null){
    val intent = Intent(activity.baseContext, ActivityQuestionnaire::class.java)
    intent.putExtra("settings", settings.toJsonObject())
    if (obResult != null)
        intent.putExtra("obResult", obResult.toJsonObject())
    activity.startActivity(intent)
}