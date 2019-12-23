package questionnaire

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class Question(
    var question: String, val statements: Statements, var truth: String

): JsonObject {

    var decription = ""
    var icon = ""
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
                val question = jsonObject["question"].toString()
                val statements = Statements.createStatements(jsonObject["statements"].toString())
                val truth = jsonObject["truth"].toString()

                Question(question, statements!!, truth).apply {
                    decription = jsonObject["decription"].toString()
                    type = jsonObject["type"].toString().toInt()
                    icon = jsonObject["icon"].toString()
                }
            }
            catch (ex: Exception) {
                null
            }
        }

    }
	
    override fun toJsonObject(): String {
        return """
            {
                "question": "$question",
                "description": "$decription",
                "type": $type,
                "statements": ${statements.toJsonObject()},
                "truth": "$truth",
                "icon": "$icon"
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