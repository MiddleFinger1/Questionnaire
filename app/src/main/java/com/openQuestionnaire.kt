package com

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.json.questionnaire.Settings
import com.json.user.ObResult
import com.ui.event.ActivityQuestionnaire


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