package questionnaire

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Source(val path: String, val type: Int = TYPE_IMAGE): JsonObject {

	companion object {
		
		const val TYPE_IMAGE = 0
		const val TYPE_DOCUMENT = 1
		const val TYPE_SONG = 2
		
		fun createSourse(json: String) = 
			try {
				createSourse(JSONParser().parse(json) as JSONObject)
			}
			catch(ex: Exception){
				null
			}
			
		fun createSourse(jsonObject: JSONObject): Source? {
			return try {
				val jsonPath = jsonObject[PATH]
				val jsonType = jsonObject[TYPE]

				if (jsonPath != null && jsonType != null)
					Source(jsonPath.toString(), jsonType.toString().toInt())
				else null
			}
			catch (ex: Exception){
				null
			}
		}
	}
	
    override fun toJsonObject(): String {
        return """
			{
				"$PATH": "$path",
				"$TYPE": $type
			}
		"""
    }
}