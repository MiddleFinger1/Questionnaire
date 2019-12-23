package questionnaire

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

object JsonParser {

    const val QUESTIONNAIRE = "questionnaire"
    const val QUESTIONS = "questions"

    fun parse(file: File){
        try {
            parse(FileReader(file))
        }
        catch (ex: Exception){
            println(ex.toString())
        }
    }

    fun parse(fileReader: FileReader){
        val jsonObject = JSONParser().parse(fileReader) as JSONObject

        val questions = jsonObject[QUESTIONS] as JSONArray
        println(questions[0])

        val question = questions[0] as JSONObject
        println(question["statements"].toString())

        println(Statements.createStatements(question["statements"] as JSONObject))

    }

    fun parse(json: String){
        try {
            val jsonParser = JSONParser()
            val jsonObject = jsonParser.parse(json) as JSONObject
            // получение строки из объекта
            //val questions = jsonObject[QUESTIONS] as JSONArray
        }
        catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        }
        catch (ex: IOException) {
            ex.printStackTrace()
        }
        catch (ex:ParseException) {
            ex.printStackTrace()
        }
        catch (ex:NullPointerException) {
            ex.printStackTrace()
        }
    }
}