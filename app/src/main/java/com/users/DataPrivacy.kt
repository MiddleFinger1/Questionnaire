package com.users

import com.questionnaire.JsonObject
import org.json.simple.JSONObject


data class DataPrivacy(
	val email: String, val password: String, val phone: String = ""
): JsonObject {
	
	companion object {
	
		fun createDataPrivacy(json: String): DataPrivacy? {
			return null
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