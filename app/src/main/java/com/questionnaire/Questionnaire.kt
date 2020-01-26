package com.questionnaire

import android.util.Log
import com.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Questionnaire(var settings: Settings

): ArrayList<Question>(), JsonObject {

    var description = ""
    var analytics = Analytics()
    var resources = arrayListOf<Source>()
    var isRandom = false
    var maxQuestions = 0
    var version = 0.0

    companion object {

        fun createQuestionnaire(json: String) =
            try{
                createQuestionnaire(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                Log.e("ex", ex.toString())
                Log.e("ex", ex.stackTrace.toString())
                null
            }

        fun createQuestionnaire(jsonObject: JSONObject): Questionnaire? {
            return try {
                val settings = Settings.createSettings(jsonObject[SETTINGS].toString())
                Questionnaire(settings).apply {
                    description = jsonObject[DESCRIPTION].toString()

                    val jsonIsRandom = jsonObject[IS_RANDOM]
                    if (jsonIsRandom != null)
                        isRandom = jsonIsRandom.toString().toBoolean()

                    val analytics = Analytics.createAnalytics(jsonObject[ANALYTICS].toString())
                    if (analytics != null)
                        this.analytics = analytics

                    val jsonVersion = jsonObject[VERSION]
                    if (jsonVersion != null)
                        version = jsonVersion.toString().toDouble()

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
                    val jsonMaxQuestions = jsonObject[MAX_QUESTIONS]
                    maxQuestions = (jsonMaxQuestions ?: size).toString().toInt()
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
        for (i in 0..this.lastIndex) {
            val question = this[i]
            questions +=
                if (i < lastIndex) "{${question.toJsonObject()}},\n"
                else "{${question.toJsonObject()}}"
        }
        var resources = ""
        for (i in 0..this.resources.lastIndex){
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
                "$ANALYTICS": {${analytics.toJsonObject()}},
                "$IS_RANDOM": $isRandom,
                "$MAX_QUESTIONS": $maxQuestions,
                "$VERSION": $version
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