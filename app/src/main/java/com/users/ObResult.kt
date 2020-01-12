package com.users

import android.util.Log
import com.*
import com.questionnaire.*

class ObResult: ArrayList<ObResult.ItemResult>(), JsonObject {

    var isPresentedTruth = false
    var cost = 0.0

    inner class ItemResult(
        val question: String, val answer: String, val truth: String
    ) : JsonObject {

        override fun toJsonObject() =
            """
                {
                    "$QUESTION": "$question",
                    "$ANSWER": "$answer",
                    "$TRUTH": "$truth"
                }
            """
    }

    fun addAnswer(question: Question, answer: ArrayList<Int>){
        var answerString = ""
        var truth = ""
        for (id in 0..question.truth.lastIndex)
            truth += "${id + 1}) " + question.statements[id] + "\n"

        Log.e("ex", question.toJsonObject())

        for (id in 0..answer.lastIndex)
            answerString += "${id + 1}) " + question.statements[id] + "\n"
        add(ItemResult(question.question, answerString, truth))
        Log.e("ex", this[lastIndex].toJsonObject())
        cost += question.cost
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