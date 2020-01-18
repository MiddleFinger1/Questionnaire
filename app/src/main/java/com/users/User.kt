package com.users

import android.util.Log
import com.*
import com.questionnaire.Settings
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class User: JsonObject {

    var isLogIn = false
    var theme = LIGHT_THEME
    lateinit var settings: com.users.Settings
    lateinit var privacy: DataPrivacy
    lateinit var analytics: Analytics
    val questionnaires = arrayListOf<Settings>()

    companion object {

        const val LIGHT_THEME = "lightTheme"
        const val DARK_THEME = "darkTheme"

        fun createUser(json: String) =
            try {
                createUser(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception) {
                Log.e("ex", ex.toString())
                null
            }

        fun createUser(jsonObject: JSONObject) =
            User().apply {
                val jsonIsLogIn = jsonObject[IS_LOG_IN]
                if (jsonIsLogIn != null)
                    isLogIn = jsonIsLogIn.toString().toBoolean()

                val jsonSettings = com.users.Settings.createSettings(jsonObject[SETTINGS].toString())

                Log.e("settings", jsonSettings.toString())

                val jsonAnalytics = Analytics.createAnalytics(jsonObject[ANALYTICS].toString())
                if (jsonAnalytics != null)
                    analytics = jsonAnalytics

                if (jsonSettings != null)
                    settings = jsonSettings

                val jsonPrivacy = DataPrivacy.createDataPrivacy(jsonObject[DATA_PRIVACY].toString())
                if (jsonPrivacy != null)
                    privacy = jsonPrivacy

                val jsonAppConfig = jsonObject[APP_CONFIG]
                if (jsonAppConfig != null)
                    theme = jsonAppConfig.toString()

                val jsonArray = jsonObject[QUESTIONS] as? JSONArray
                if (jsonArray != null)
                    for (item in jsonArray)
                        questionnaires.add(Settings.createSettings(item.toString()))
            }
    }

    override fun toJsonObject(): String {
        var jsonArray = ""
        for (id in 0..questionnaires.lastIndex) {
            val item = questionnaires[id]
            jsonArray +=
                if (id != questionnaires.lastIndex)
                    "${item.toJsonObject()},"
                else item.toJsonObject()
        }
        return """
            {
                "$IS_LOG_IN": $isLogIn,
                "$APP_CONFIG": "$theme",
                "$DATA_PRIVACY": ${privacy.toJsonObject()},
                "$ANALYTICS": $analytics,
                "$SETTINGS": ${settings.toJsonObject()},
                "$QUESTIONS": [$jsonArray]
            }
        """
    }
}