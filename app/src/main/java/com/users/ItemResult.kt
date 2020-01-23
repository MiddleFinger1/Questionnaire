package com.users

import android.util.Log
import com.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.lang.Exception


class ItemResult(
    val question: String, val answer: String, val truth: Boolean, val thuthAnswer: String = ""
) : JsonObject {

    var array = arrayListOf<Int>()

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
            val jsonAnswer = jsonObject[ANSWER]
            val jsonTruth = jsonObject[TRUTH]
            val jsonTruthString = jsonObject[TRUTH_STRING]

            return if (jsonQuestion != null && jsonAnswer != null && jsonTruth != null && jsonTruthString != null)
                ItemResult(
                    jsonQuestion.toString(),
                    jsonAnswer.toString(),
                    jsonTruth.toString().toBoolean(),
                    jsonTruthString.toString())
            else null
        }
    }

    override fun toJsonObject() =
        """
            {
                "$QUESTION": "$question",
                "$ANSWER": "$answer",
                "$TRUTH_STRING": "$thuthAnswer",
                "$TRUTH": $truth
            }
        """
}