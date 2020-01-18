package com.users

import android.util.Log
import com.JsonObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser


class Analytics: ArrayList<ObResult>(), JsonObject {

    fun addElements(json: String) =
        addElements(JSONParser().parse(json) as JSONArray)

    fun addElements(jsonArray: JSONArray) {
        for (item in jsonArray){
            val obResult = ObResult.createObResult(item.toString())
            if (obResult != null)
                add(obResult)
        }
    }

    companion object {

        fun createAnalytics(json: String) =
            try {
                Analytics().apply {
                    addElements(json)
                }
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                null
            }
    }

    override fun toJsonObject(): String {
        var jsonItems = ""
        for (id in 0..lastIndex){
            val item = this[id]
            jsonItems +=
                if (id == lastIndex)
                    "${item.toJsonObject()},"
                else item.toJsonObject()
        }
        return "[$jsonItems]"
    }
}