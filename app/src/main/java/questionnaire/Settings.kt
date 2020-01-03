package questionnaire

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Settings(var title: String): JsonObject {

    var group = NONE_GROUP
    val id = -1
    var mark: Double = 0.0
    private set(value) = Unit
    var privacy = PUBLIC
	var userId = -1
	var path = ""
    var isInSd = true
    var icon: Source? = null

    companion object {
        const val PUBLIC = 0
        const val PRIVATE = 1
        const val NONE_GROUP = "none group"

        fun createSettings(json: String) =
            createSettings(JSONParser().parse(json) as JSONObject)

        fun createSettings(jsonObject: JSONObject) =
            Settings(jsonObject[TITLE].toString()).apply {

                val jsonGroup = jsonObject[GROUP]
                if (jsonGroup != null)
                    group = jsonGroup.toString()

                val jsonMark = jsonObject[MARK]
                if (jsonMark != null)
                    mark = jsonMark.toString().toDouble()

                val jsonPrivacy = jsonObject[PRIVACY]
                if (jsonPrivacy != null)
                    privacy = jsonPrivacy.toString().toInt()

                val jsonPath = jsonObject[PATH]
                if (jsonPath != null)
				    path = jsonPath.toString()

                val jsonIdUser = jsonObject[USER_ID]
                if (jsonIdUser != null)
				    userId = jsonIdUser.toString().toInt()

                val jsonIcon = jsonObject[ICON]
                if (jsonIcon != null)
                    icon = Source.createSource(jsonIcon.toString())
            }
    }

    private val marks = arrayListOf<Double>()

    fun addMark(mark: Double){
        var commonMark = 0.0
        for (existMark in marks)
            commonMark += existMark
        commonMark += mark
        this.mark = mark / (marks.size + 1)
    }

    override fun toJsonObject(): String {
        return """
           {
                "$ID": $id,
                "$IS_IN_SD": $isInSd,
				"$USER_ID": $userId,
                "$GROUP": "$group",
                "$TITLE": "$title",
                "$MARK": $mark,
                "$PRIVACY": $privacy,
				"$PATH": "$path"
           }
        """
        /*
            {
                "id": -1            // айди анкеты
                "isInSd"
				"userId": -1		// айди пользователя
                "group": "", 		// к какой группе относиться (обычно это мб имеет отношение к пользователю или тегу)
                "title": "", 		// название анкеты
                "mark": 0.0,         // общая оценка
                "privacy": 0,       // доступ к анкете
				"path": ""			// путь к анкете (в json разметке) .json
            }
         */
    }
}