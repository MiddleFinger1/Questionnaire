package com.users

import com.*
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser


data class DataPrivacy(
    val email: String, val password: String, val phone: String = "", val userID: Int

): JsonObject {

    companion object {

        fun createDataPrivacy(json: String) =
            try {
                createDataPrivacy(JSONParser().parse(json) as JSONObject)
            }
            catch (ex: Exception){
                null
            }

        fun createDataPrivacy(jsonObject: JSONObject): DataPrivacy? {
            val email = jsonObject[EMAIL]
            val password = jsonObject[PASSWORD]
            val phone = jsonObject[PHONE]
            val userID = jsonObject[USER_ID]

            return if (email != null && password != null && phone != null && userID != null)
                DataPrivacy(
                    email.toString(),
                    password.toString(),
                    phone.toString(),
                    userID.toString().toInt()
                )
            else null
        }
    }

    override fun toJsonObject() =
        """
		{
			"$EMAIL": "$email",
			"$PASSWORD": "$password",
			"$PHONE": "$phone",
            "$USER_ID": $userID
		}
	"""
}