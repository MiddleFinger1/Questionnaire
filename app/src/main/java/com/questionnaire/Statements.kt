package com.questionnaire

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Statements: ArrayList<String>(), JsonObject {

    var type = SINGLE

    companion object {

        fun createStatements(json: String) =
            try {
                createStatements(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                null
            }

        fun createStatements(Object: JSONObject): Statements? {
            return try {
                 Statements().apply {
                    type = Object["type"].toString().toInt()
                    for (statement in Object["statements"] as JSONArray)
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
        for (i in 0..size) {
            val statement = this[i]
            statements +=
                if (i == lastIndex) "\"$statement\","
                else "\"$statement\""
        }
        return """
           {
                "type": $type,
                "statements": [$statements]
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

