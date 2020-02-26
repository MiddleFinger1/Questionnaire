package com.json.user

import android.util.Log
import com.*
import com.json.JsonObject
import com.json.questionnaire.Source
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


data class Settings(
    val icon: Source, val login: String
): JsonObject {

    var path = ""

    companion object {

        fun createSettings(json: String) =
            try {
                createSettings(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("ex_settings", ex.toString())
                Log.e("ex_settings", json)
                null
            }

        fun createSettings(jsonObject: JSONObject): Settings? {
            val jsonIcon = Source.createSource(jsonObject[ICON].toString())
            val jsonLogin = jsonObject[LOGIN]
            return if (jsonIcon != null && jsonLogin != null){
                Settings(jsonIcon, jsonLogin.toString()).apply {
                    val pathJson = jsonObject[PATH]
                    if (pathJson != null)
                        path = pathJson.toString()
                }
            }
            else null
        }
    }

    override fun toJsonObject(): String {
        return """
            {
                "$ICON": ${icon.toJsonObject()},
                "$LOGIN": "$login",
                "$PATH": "$path"
            }
        """
    }
}