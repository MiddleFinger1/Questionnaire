package com.questionnaire

import android.util.Log
import com.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import kotlin.random.Random


class Question(
    var question: String, val statements: Statements

): JsonObject {

    val truth = arrayListOf<Int>()
    var description = ""
    var icon: Source? = null
    var isDefault = true
	var isBlocked = false
	var time = 0L
    lateinit var context: Questionnaire
	var cost = 1.0

    companion object {

        fun createQuestion(json: String) =
            try {
                createQuestion(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                null
           }

        fun createQuestion(jsonObject: JSONObject): Question? {
            return try {
                val question = jsonObject[QUESTION]
                val statements = Statements.createStatements(jsonObject[STATEMENTS].toString())
                val jsonTruth = jsonObject[TRUTH] as? JSONArray

                return if (question != null && statements != null && jsonTruth != null){
                    Question(question.toString(), statements).apply {
                        try {
                            for (item in jsonTruth)
                                truth += item.toString().toInt()
                        }
                        catch (ex: Exception){
                            Log.e("ex", ex.toString())
                        }
                        if (statements.isRandom && statements.size > 1)
                            random()
                        description = jsonObject[DESCRIPTION].toString()

                        val jsonIsDefault = jsonObject[IS_DEFAULT]
                        if (jsonIsDefault != null)
                            isDefault = jsonIsDefault.toString().toBoolean()
						
						val jsonIsBlocked = jsonObject[IS_BLOCKED]
						if (jsonIsBlocked != null)
							isBlocked = jsonIsBlocked.toString().toBoolean()
						
						val jsonTime = jsonObject[TIME]
						if (jsonTime != null)
							time = jsonTime.toString().toLong()

                        val jsonIcon = jsonObject[ICON]
                        if (jsonIcon != null)
                            icon = Source.createSource(jsonIcon.toString())
							
						val jsonCost = jsonObject[COST]
						if (jsonCost != null)
							cost = jsonCost.toString().toDouble()
                    }
                }
                else null
            }
            catch (ex: Exception) {
                null
            }
        }
    }

    private fun random(){
        val trues = arrayListOf<Int>()
        val array = arrayListOf<String>()
        val range = arrayListOf<Int>()
        range.addAll(0..statements.lastIndex)
        while (range.isNotEmpty()){
            val int = Random(System.currentTimeMillis()).nextInt(range.size)
            array.add(statements[range[int]])
            if (truth.indexOf(range[int]) >= 0)
                trues.add(array.lastIndex)
            range.removeAt(int)
        }
        statements.clear()
        truth.clear()
        statements.addAll(array)
        truth.addAll(trues)
        Log.e("ex", statements.toString())
    }

    override fun toJsonObject(): String {
        return """
            {
                "$QUESTION": "$question",
                "$DESCRIPTION": "$description",
                "$IS_DEFAULT": $isDefault,
				"$IS_BLOCKED": $isBlocked,
                "$STATEMENTS": ${statements.toJsonObject()},
                "$TRUTH": $truth,
				"$TIME": $time,
                "$ICON": "${icon?.toJsonObject()}",
				"$COST": $cost
            }
        """
    }
	
	/*
	{
      "question": "", 			// сам вопрос
      "description": "", 			// описание
      "type": 0, 					// тип вопроса (0 - обычный, 1 - на время)
      "statements": {},			// выборы ответов
      "truth": "",					// правильный ответ
      "icon": ""					// иконка к вопросу или документ
    }
	*/
	
}