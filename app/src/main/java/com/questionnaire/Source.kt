package com.questionnaire

import android.util.Log
import com.IS_IN_SD
import com.JsonObject
import com.PATH
import com.TYPE
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


class Source(val path: String, val type: Int = TYPE_IMAGE): JsonObject {
	
	var isInSd = true
	
	companion object {
		
		const val TYPE_IMAGE = 0
		const val TYPE_DOCUMENT = 1
		const val TYPE_SONG = 2
		const val TYPE_LINK = 3
		
		fun createSource(json: String) =
			try {
				createSource(JSONParser().parse(json) as JSONObject)
			}
			catch(ex: Exception){
				Log.e("ex", ex.toString())
				null
			}
			
		fun createSource(jsonObject: JSONObject): Source? {
			return try {
				val jsonPath = jsonObject[PATH]
				val jsonType = jsonObject[TYPE]
				val jsonIsInSd = jsonObject[IS_IN_SD]

				Log.e("json", jsonPath.toString())
				Log.e("json", jsonType.toString())
				Log.e("json", jsonIsInSd.toString())

				if (jsonPath != null && jsonType != null)
					Source(jsonPath.toString(), jsonType.toString().toInt()).apply {
						if (jsonIsInSd != null)
							isInSd = jsonIsInSd.toString().toBoolean()
					}
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
				"$IS_IN_SD": $isInSd,
				"$PATH": "$path",
				"$TYPE": $type
			}
		"""
    }
}