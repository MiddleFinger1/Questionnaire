package com.users

import com.questionnaire.JsonObject
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


data class DataPrivacy(
	val email: String, val password: String, val phone: String = ""
): JsonObject {
	
	companion object {
	
		fun createDataPrivacy(json: String) =
			try {
				createDataPrivacy(JSONParser().parse(json) as JSONObject)
			}
			catch (ex: Exception){
				null
			}
		
		fun createDataPrivacy(json: String, check: Boolean): DataPrivacy? {
			return null
		}
		
		fun createDataPrivacy(jsonObject: JSONObject): DataPrivacy? {
			return null
		}
	
	}

	override fun toJsonObject() = 
	"""
		{
			"$EMAIL": "$email",
			"$PASSWORD": "$password",
			"$PHONE": "$phone"
		}
	"""
	
	/*
		
	*/
}