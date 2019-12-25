package questionnaire

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Analytics: JsonObject {

    companion object {

        fun createAnalytics(json: String) =
            createAnalytics(JSONParser().parse(json) as? JSONObject)

        fun createAnalytics(jsonObject: JSONObject?) =
            Analytics().apply {

            }
    }

    override fun toJsonObject(): String {
        return ""
    }
}