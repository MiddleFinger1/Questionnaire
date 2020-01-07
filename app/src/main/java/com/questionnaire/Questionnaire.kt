package com.questionnaire

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Questionnaire(var settings: Settings

): ArrayList<Question>(), JsonObject {

    var description = ""
    var analytics = Analytics()
    var resources = arrayListOf<Source>()

    companion object {

        fun createQuestionnaire(json: String) =
            try{
                createQuestionnaire(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                println(ex.toString())
                println(ex.stackTrace)
                null
            }

        fun createQuestionnaire(jsonObject: JSONObject): Questionnaire? {
            return try {
                val settings = Settings.createSettings(jsonObject[SETTINGS].toString())
                Questionnaire(settings).apply {
                    description = jsonObject[DESCRIPTION].toString()

                    val analytics = Analytics.createAnalytics(jsonObject[Analytics].toString())
                    if (analytics != null)
                        this.analytics = analytics

                    for (item in jsonObject[RESOURCES] as JSONArray) {
                        val source = Source.createSource(item.toString())
                        if (source is Source)
                            resources.add(source)
                    }

                    for (item in jsonObject[QUESTIONS] as JSONArray){
                        val question = Question.createQuestion(item.toString())
                        if (question != null){
                            question.context = this
                            add(question)
                        }
                    }
                }
            }
            catch (ex: Exception){
                println(ex.toString())
                println(ex.stackTrace)
                null
            }
        }
    }
	
    override fun toJsonObject(): String {
        var questions = ""
        for (i in 0..this.size) {
            val question = this[i]
            questions +=
                if (i < lastIndex) "{${question.toJsonObject()}},\n"
                else "{${question.toJsonObject()}}"
        }
        var resources = ""
        for (i in 0..this.resources.size){
            val resource = this.resources[i]
            resources +=
                if (i < lastIndex) "\"${resource.toJsonObject()}\","
                else "\"${resource.toJsonObject()}\""
        }
        return """
            {
                "$DESCRIPTION": "$description",
                "$QUESTIONS": "[$questions]",
                "$RESOURCES": [$resources]
                "$SETTINGS": {${settings.toJsonObject()}}
                "$ANALYTICS": {${analytics.toJsonObject()}}
            }
        """
    }
	
	/*
	{
	    "description": "", 	// описание анкеты
		"questions": [],	// список вопросов
		"resources": [], 	// внутренние ресурсы анкеты (источники)
		"settings": {},     // найстройки анкеты и методы его теги поиска
		"analytics": {}
	}
	*/
}