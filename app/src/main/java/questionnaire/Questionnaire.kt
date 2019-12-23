package questionnaire


class Questionnaire(
    var title: String, var description: String = ""

): ArrayList<Question>(), JsonObject {

	val group = NONE_GROUP
    lateinit var settings: Settings
    lateinit var analitics: Analitics
    var sourse: Source? = null

    companion object {
		
		const val NONE_GROUP = "none group"
		
        fun toCreateQuestionnaire(json: String): Questionnaire? {

            return null
        }
    }
	
    override fun toJsonObject(): String {
        return ""
    }
	
	/*
	{	
		"title": "", 		// название анкеты
		"group": "", 		// к какой группе относиться (обычно это мб имеет отношение к пользователю или тегу)
		"description": "", 	// описание анкеты
		"questions": [],	// список вопросов
		"icon": "",			// иконка для анкеты
		"resources": [], 	// внутренние ресурсы анкеты (источники)
		"settings": {},
		"analitics": {}
	}
	*/
}