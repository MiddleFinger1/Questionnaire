package com.questionnaire

import android.util.Log
import com.JsonObject
import com.MARKS
import com.POINTS
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Analytics: JsonObject {

    val marks = arrayListOf<String>()
    val points = arrayListOf<Double>()

    companion object {

        fun createAnalytics(json: String) =
            try {
                Log.e("ex", json)
                createAnalytics(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                null
            }

        fun createAnalytics(jsonObject: JSONObject) =
            try {
                Analytics().apply {
                    val marks = jsonObject[MARKS] as? JSONArray
                    val points = jsonObject[POINTS] as? JSONArray
                    if (marks != null && points != null){
                        for (id in 0..marks.lastIndex){
                            this.marks += marks[id].toString()
                            this.points += points[id].toString().toDouble()
                        }
                    }
                }
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                null
            }
    }

    override fun toJsonObject(): String {
        return """
            {
                "$MARKS": $marks,
                "$POINTS": $points
            }
        """
    }
}