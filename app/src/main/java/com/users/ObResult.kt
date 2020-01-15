package com.users

import android.util.Log
import com.*
import com.questionnaire.*
import java.lang.Exception

class ObResult: ArrayList<ObResult.ItemResult>(), JsonObject {

    var isPresentedTruth = false
    var cost = 0.0

    inner class ItemResult(
        val question: String, val answer: ArrayList<Int>, val truth: ArrayList<Int>
    ) : JsonObject {

        override fun toJsonObject() =
            """
                {
                    "$QUESTION": "$question",
                    "$ANSWER": $answer,
                    "$TRUTH": $truth
                }
            """
    }

    fun addAnswer(id: Int, question: Question, answer: ArrayList<Int>){
        Log.e("ex", id.toString())

        try {
            if (id >= size) {
                add(ItemResult(question.question, answer, question.truth))
            } else this[id] = ItemResult(question.question, answer, question.truth)
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
        }
        Log.e("ex", question.toJsonObject())
        //Log.e("ex", this[id].toJsonObject())
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