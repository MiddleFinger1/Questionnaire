package com.questionnaire.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.Helper
import com.SETTINGS
import com.questionnaire.Settings
import com.users.ObResult


fun openQuestionnaire(activity: AppCompatActivity, settings: Settings, obResult: ObResult? = null){
    val intent = Intent(activity.baseContext, ActivityQuestionnaire::class.java)
    var json =
        if (settings.isInSd)
            Helper.converting(activity.assets.open(settings.path))
        else ""
    json = json.replace(""""$SETTINGS": {}""", """"$SETTINGS": ${settings.toJsonObject()}""")
    intent.putExtra("questionnaire", json)
    Log.e("jsonQuestionnaire", json)
    if (obResult != null)
        intent.putExtra("obResult", obResult.toJsonObject())
    activity.startActivity(intent)
}