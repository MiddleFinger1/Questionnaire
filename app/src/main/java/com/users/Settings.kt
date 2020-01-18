package com.users

import android.util.Log
import com.*
import com.questionnaire.Source
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


data class Settings(
    val icon: Source, val login: String, val userID: Int
): JsonObject {

    companion object {

        fun createSettings(json: String) =
            try {
                createSettings(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("ex_settings", ex.toString())
                null
            }

        fun createSettings(jsonObject: JSONObject): Settings? {
            val jsonIcon = Source.createSource(jsonObject[ICON].toString())
            val jsonLogin = jsonObject[LOGIN]
            val jsonUserID = jsonObject[USER_ID]
            return if (jsonIcon != null && jsonLogin != null){
                Settings(jsonIcon, jsonLogin.toString(), jsonUserID.toString().toInt())
            }
            else null
        }
    }

    override fun toJsonObject(): String {
        return """
            {
                "$ICON": ${icon.toJsonObject()},
                "$LOGIN": "$login",
                "$ID": $userID"
            }
        """
    }
}