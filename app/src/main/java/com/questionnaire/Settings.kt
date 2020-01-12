package com.questionnaire

import com.*
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Settings(var title: String): JsonObject {

    var isAnonymous = false
    var isPresented = false
    var group = NONE_GROUP
    val id = -1
    var mark: Double = 0.0
    private set(value) = Unit
    var isPrivate = false
	var userId = -1
	var path = ""
    var isInSd = true
    var icon: Source? = null
    var password = ""

    companion object {

        const val NONE_GROUP = "none group"

        fun createSettings(json: String) =
            createSettings(JSONParser().parse(json) as JSONObject)

        fun createSettings(jsonObject: JSONObject) =
            Settings(jsonObject[TITLE].toString()).apply {

                val jsonIsAnonymous = jsonObject[IS_ANONYMOUS]
                if (jsonIsAnonymous != null)
                    isAnonymous = jsonIsAnonymous.toString().toBoolean()

                val jsonGroup = jsonObject[GROUP]
                if (jsonGroup != null)
                    group = jsonGroup.toString()

                val jsonMark = jsonObject[MARK]
                if (jsonMark != null)
                    mark = jsonMark.toString().toDouble()

                val jsonIsPrivacy = jsonObject[IS_PRIVATE]
                if (jsonIsPrivacy != null)
                    isPrivate = jsonIsPrivacy.toString().toBoolean()

                val jsonPath = jsonObject[PATH]
                if (jsonPath != null)
				    path = jsonPath.toString()

                val jsonIdUser = jsonObject[USER_ID]
                if (jsonIdUser != null)
				    userId = jsonIdUser.toString().toInt()

                val jsonIcon = jsonObject[ICON]
                if (jsonIcon != null)
                    icon = Source.createSource(jsonIcon.toString())

                val jsonPassword = jsonObject[PASSWORD]
                if (jsonPassword != null)
                    password = jsonPassword.toString()

                val jsonIsPresented = jsonObject[IS_PRESENTED]
                if (jsonIsPresented != null)
                    isPresented = jsonIsPresented.toString().toBoolean()
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
                "$IS_ANONYMOUS": $isAnonymous,
                "$IS_PRESENTED": $isPresented,
                "$IS_PRIVATE": $isPrivate,
				"$PATH": "$path",
                "$ICON": ${icon?.toJsonObject()},
                "$PASSWORD": "$password"
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