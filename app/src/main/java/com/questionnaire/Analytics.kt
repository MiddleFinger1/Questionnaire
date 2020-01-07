package com.questionnaire

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Analytics: JsonObject {

    var marks = mutableMapOf<Int, Double>()

    companion object {

        fun createAnalytics(json: String) =
            try {
                createAnalytics(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                null
            }

        fun createAnalytics(jsonObject: JSONObject) =
            try {
                val analytics = Analytics()
                val array = jsonObject[DESCRIBES] as? JSONArray
                if (array != null){
                    for (id in array){
                        val got = id.toString().toInt()
                        val mark = array[got].toString().toDouble()
                        analytics.marks[got] = mark
                    }
                }
                analytics
            }
            catch (ex: Exception){
                null
            }
    }

    override fun toJsonObject(): String {
        var json = ""
        for (got in marks.keys){
            val mark = marks[got]
            json += if (marks.size == marks.keys.indexOf(got))
                "{$got: $mark}"
                else "{$got: $mark}, "
        }
        return """
            {
                "$DESCRIBES": [$json]"
            }
        """
    }
}