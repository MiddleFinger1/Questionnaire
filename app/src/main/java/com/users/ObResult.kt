package com.users

import android.util.Log
import com.*
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ObResult: ArrayList<ItemResult>(), JsonObject {

    var id = -1
    var isPresentedTruth = false
    var cost = 0.0
    var tries = 0
    var dateTry: Calendar? = null

    companion object {

        fun createObResult(json: String) =
            try {
                createObResult(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("exCreateResult", ex.toString())
                null
            }

        private fun createObResult(jsonObject: JSONObject) =
            ObResult().apply {
                val jsonIsPresented = jsonObject[IS_PRESENTED]
                if (jsonIsPresented != null)
                    isPresentedTruth = jsonIsPresented.toString().toBoolean()
                val jsonCost = jsonObject[COST]
                if (jsonCost != null)
                    cost = jsonCost.toString().toDouble()
                val jsonId = jsonObject[ID]
                if (jsonId != null)
                    id = jsonId.toString().toInt()
                val triesJson = jsonObject[TRIES]
                if (triesJson != null)
                    tries = triesJson.toString().toInt()
                val dateTriesJson = jsonObject[DATE_TRY]
                if (dateTriesJson != null)
                    dateTry = Helper.stringToCalendar(dateTriesJson.toString())
                val jsonArray = jsonObject[QUESTIONS] as? JSONArray
                if (jsonArray != null){
                    for (item in jsonArray) {
                        val itemResult = ItemResult.createItemResult(item.toString())
                        if (itemResult != null)
                            add(itemResult)
                    }
                }
            }
    }

    override fun toJsonObject(): String {
        var jsonItems = ""
        for (id in 0..this.lastIndex) {
            val item = this[id]
            jsonItems +=
                if (id != lastIndex) "${item.toJsonObject()},"
                else item.toJsonObject()
        }
        return """
            {
                "$ID": $id,
                "$IS_PRESENTED": $isPresentedTruth,
                "$QUESTIONS": [$jsonItems],
                "$COST": $cost,
                "$TRIES": $tries,
                "$DATE_TRY": "${Helper.calendarToString(dateTry)}"
            }
        """
    }
}