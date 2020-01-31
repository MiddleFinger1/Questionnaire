package com.json.user

import android.util.Log
import com.json.JsonObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser


class Analytics: ArrayList<ObResult>(), JsonObject {

    fun addElements(json: String) {
        addElements(JSONParser().parse(json) as JSONArray)
    }

    private fun addElements(jsonArray: JSONArray) {
        for (item in jsonArray){
            addElement(item.toString())
        }
    }

    fun addElement(json: String){
        val obResult = ObResult.createObResult(json)
        if (obResult != null)
            add(obResult)
        if (size > 20)
            removeAt(0)
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
                if (id != lastIndex)
                    "${item.toJsonObject()},"
                else item.toJsonObject()
        }
        return "[$jsonItems]"
    }
}