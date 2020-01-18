package com.users

import android.util.Log
import com.*
import com.questionnaire.*
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import java.lang.Exception

class ObResult: ArrayList<ItemResult>(), JsonObject {

    var isPresentedTruth = false
    var cost = 0.0

    companion object {

        fun createObResult(json: String) =
            try {
                createObResult(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                null
            }

        fun createObResult(jsonObject: JSONObject) =
            ObResult().apply {
                val jsonIsPresented = jsonObject[IS_PRESENTED]
                if (jsonIsPresented != null)
                    isPresentedTruth = jsonIsPresented.toString().toBoolean()
                val jsonCost = jsonObject[COST]
                if (jsonCost != null)
                    cost = jsonCost.toString().toDouble()
                val jsonArray = jsonObject[QUESTIONS]
                if (jsonArray != null){
                    for (item in jsonArray as JSONArray) {
                        val itemResult = ItemResult.createItemResult(item.toString())
                        if (itemResult != null)
                            add(itemResult)
                    }
                }
            }
    }

    fun addAnswer(id: Int, question: Question, answer: ArrayList<Int>){
        Log.e("ex", id.toString())
        try {
            if (id >= size) {
                add(ItemResult(question.question, answer, question.truth))
            } else this[id] = ItemResult(question.question, answer, question.truth)
            cost += question.cost
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
        }
        Log.e("ex", question.toJsonObject())
    }

    override fun toJsonObject(): String {
        var jsonItems = ""
        for (id in 0..this.size) {
            val item = this[id]
            jsonItems +=
                if (id != lastIndex) "${item.toJsonObject()},"
                else item.toJsonObject()
        }
        return """
            {
                "$IS_PRESENTED": $isPresentedTruth,
                "$QUESTIONS": [$jsonItems],
                "$COST": $cost
            }
        """
    }
}