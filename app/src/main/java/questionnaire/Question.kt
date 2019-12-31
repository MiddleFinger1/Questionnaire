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
                val question = jsonObject[QUESTION]
                val statements = Statements.createStatements(jsonObject[STATEMENTS].toString())
                val truth = jsonObject[TRUTH]

                return if (question != null && statements != null && truth != null){
                    Question(question.toString(), statements, truth.toString()).apply {
                        decription = jsonObject[DESCRIPTION].toString()
                        type = jsonObject[TYPE].toString().toInt()
                        icon = jsonObject[ICON].toString()
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
                "$TRUTH": "$truth",
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