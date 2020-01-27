package com.questionnaire

import com.IS_RANDOM
import com.JsonObject
import com.STATEMENTS
import com.TYPE
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Statements: ArrayList<String>(), JsonObject {

    var type = SINGLE
    var isRandom = false
    var entered = ""

    companion object {

        fun createStatements(json: String) =
            try {
                createStatements(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                null
            }

        fun createStatements(jsonObject: JSONObject): Statements? {
            return try {
                 Statements().apply {

                     val jsonType = jsonObject[TYPE]
                     if (jsonType != null)
                         type = jsonType.toString().toInt()

                     val jsonIsRandom = jsonObject[IS_RANDOM]
                     if (jsonIsRandom != null)
                         isRandom = jsonIsRandom.toString().toBoolean()

                     for (statement in jsonObject[STATEMENTS] as JSONArray)
                        add(statement.toString())
                }
            }
            catch (ex: Exception){
                null
            }
        }

        const val SINGLE = 0
        const val MULTI = 1
        const val ENTER = 2
    }

    override fun toJsonObject(): String {
        
        var statements = ""
        for (i in 0..lastIndex) {
            val statement = this[i]
            statements +=
                if (i == lastIndex) "\"$statement\""
                else "\"$statement\","
        }
        return """
           {
                "$TYPE": $type,
                "$IS_RANDOM": $isRandom,
                "$STATEMENTS": [$statements]
           }
        """
    }
	
	/*
	{
		"type": 0,			// тип группы (0 - только один ответ, 1 - несколько вариантов, 2 - ответ от пользователя)
		"statements": [] 	// строковое представление ответов
	}
	*/
}

