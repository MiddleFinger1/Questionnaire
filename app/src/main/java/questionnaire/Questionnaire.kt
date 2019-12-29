package questionnaire

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Questionnaire(val settings: Settings

): ArrayList<Question>(), JsonObject {

    var description = ""
    lateinit var analytics: Analytics
    var resourses = arrayListOf<String>()

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
                        analytics = Analytics.createAnalytics(jsonObject[Analytics].toString())

                        for (item in jsonObject[RESOURCES].toString().split(","))
                            resourses.add(item)

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
        for (i in 0..this.resourses.size){
            val resource = this.resourses[i]
            resources +=
                if (i < lastIndex) "\"$resource\","
                else "\"$resource\""
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