package questionnaire


class Questionnaire(
    var title: String, var description: String = ""

): ArrayList<Question>(), JsonObject {

    lateinit var settings: Settings
    lateinit var analitics: Analitics
    var sourse: Source? = null

    companion object {

        fun toCreateQuestionnaire(json: String): Questionnaire? {

            return null
        }
    }

    override fun toJsonObject(): String {
        return ""
    }

}