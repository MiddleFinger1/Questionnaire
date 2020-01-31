package com.json.user

import android.util.Log
import com.json.JsonObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser


class Game: JsonObject {

    val arrayList = arrayListOf<ArrayList<Boolean>>()

    companion object {
        fun createGame(json: String) =
            try {
                createGame(JSONParser().parse(json) as JSONArray)
            }
            catch (ex: Exception){
                Log.e("exCreateGame", ex.toString())
                null
            }

        private fun createGame(jsonObject: JSONArray): Game {
            val game = Game()
            for (i in 0..jsonObject.lastIndex){
                game.arrayList.add(arrayListOf())
                val jsonArray = jsonObject[i] as? JSONArray
                if (jsonArray != null)
                    for (j in 0..jsonArray.lastIndex)
                        game.arrayList[i].add(jsonArray[j].toString().toBoolean())
            }
            return game
        }
    }

    override fun toJsonObject() = arrayList.toString()
}