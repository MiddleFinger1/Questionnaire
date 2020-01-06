package com.questionnaire

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.lang.NumberFormatException

class Question(
    var question: String, val statements: Statements, var truth: Int

): JsonObject {

    var decription = ""
    var icon: Source? = null
    var type = ON_DEFAULT
    lateinit var context: Questionnaire

    companion object {

        const val ON_DEFAULT = 0
        const val ON_TIME = 1

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
                val truth = try {
                    jsonObject[TRUTH].toString().toInt()
                }
                catch (ex: Exception) {
                    return null
                }
                return if (question != null && statements != null){
                    Question(question.toString(), statements, truth).apply {
                        decription = jsonObject[DESCRIPTION].toString()
                        type = jsonObject[TYPE].toString().toInt()

                        val jsonIcon = jsonObject[ICON]
                        if (jsonIcon != null)
                            icon = Source.createSource(jsonIcon.toString())
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
                "$DESCRIPTION": "$decription",
                "$TYPE": $type,
                "$STATEMENTS": ${statements.toJsonObject()},
                "$TRUTH": $truth,
                "$ICON": "$icon"
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