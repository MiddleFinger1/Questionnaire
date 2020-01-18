package com.users

import android.util.Log
import com.ANSWER
import com.JsonObject
import com.QUESTION
import com.TRUTH
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.lang.Exception


class ItemResult(
    val question: String, val answer: ArrayList<Int>, val truth: ArrayList<Int>
) : JsonObject {

    companion object {
        fun createItemResult(json: String) =
            try {
                createItemResult(JSONParser().parse(json) as JSONObject)
            } catch (ex: Exception) {
                Log.e("ex", ex.toString())
                null
            }

        fun createItemResult(jsonObject: JSONObject): ItemResult? {
            val jsonQuestion = jsonObject[QUESTION]
            val jsonAnswer = jsonObject[ANSWER] as? JSONArray
            val jsonTruth = jsonObject[TRUTH] as? JSONArray

            return if (jsonQuestion != null && jsonAnswer != null && jsonTruth != null){
                val answer = arrayListOf<Int>()
                for (item in jsonAnswer)
                    answer.add(item.toString().toInt())
                val truth = arrayListOf<Int>()
                for (item in jsonTruth)
                    truth.add(item.toString().toInt())
                ItemResult(jsonQuestion.toString(), answer, truth)
            }
            else null
        }
    }

    override fun toJsonObject() =
        """
            {
                "$QUESTION": "$question",
                "$ANSWER": $answer,
                "$TRUTH": $truth
            }
        """
}