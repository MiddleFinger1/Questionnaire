package com.users

import android.util.Log
import com.*
import com.questionnaire.*
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
    //
    fun addAnswer(id: Int, question: Question, answer: ArrayList<Int>){
        Log.e("ex", id.toString())

        //сбор текста ответов
        var answerString = ""
        for (item in answer)
            answerString += when (question.statements.type){
                Statements.ENTER -> question.statements.entered
                Statements.MULTI -> "$item) ${question.statements[item]}\n"
                Statements.SINGLE -> question.statements[item]
                else -> ""
            }
        var truthString = ""
        if (isPresentedTruth){
            for (item in question.truth)
                truthString += when (question.statements.type){
                    Statements.ENTER -> question.statements[item]
                    Statements.MULTI -> "$item) ${question.statements[item]}\n"
                    Statements.SINGLE -> question.statements[item]
                    else -> ""
                }
        }
        //проверка на схожесть ответа на правильность
        var truth = true
        for (item in question.truth)
            if (answer.indexOf(item) < 0)
                truth = false
        if (question.truth.size != answer.size)
            truth = false
        try {
            val item = ItemResult(question.question, answerString, truth, truthString)
            item.array = answer
            if (id >= size)
                add(item)
            else this[id] = item
            if (item.truth)
                cost += question.cost
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
        }
        Log.e("ex", question.toJsonObject())
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