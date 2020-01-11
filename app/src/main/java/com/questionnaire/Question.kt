package com.questionnaire

import android.util.Log
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Question(
    var question: String, val statements: Statements

): JsonObject {

    val truth = arrayListOf<Int>()
    var description = ""
    var icon: Source? = null
    var isDefault = true
    lateinit var context: Questionnaire
	var cost = 1.0

    companion object {

        fun createQuestion(json: String) =
            try {
                createQuestion(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
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
                        description = jsonObject[DESCRIPTION].toString()

                        val jsonIsDefault = jsonObject[IS_DEFAULT]
                        if (jsonIsDefault != null)
                            isDefault = jsonIsDefault.toString().toBoolean()

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
	
    override fun toJsonObject(): String {
        return """
            {
                "$QUESTION": "$question",
                "$DESCRIPTION": "$description",
                "$IS_DEFAULT": $isDefault,
                "$STATEMENTS": ${statements.toJsonObject()},
                "$TRUTH": $truth,
                "$ICON": "$icon",
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