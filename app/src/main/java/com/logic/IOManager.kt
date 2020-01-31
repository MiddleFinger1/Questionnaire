package com.logic

import android.content.Context
import android.os.Environment
import android.util.Log
import com.Helper
import com.json.questionnaire.Questionnaire
import com.json.user.User
import java.io.*


object IOManager {

    lateinit var user: User

    const val userFileName = "user.json"
    const val constructorFileName = "constructor.json"
    const val appDirectory = "Questionnaire"

    fun downloadUser(context: Context) {
        val json = readFile(userFileName)
        Log.e("jsonDownloadUser", json)
        val user = if (json != "")
            User.createUser(json)
        else createUser(context)
        if (user != null)
            IOManager.user = user
    }

    private fun createUser(context: Context): User? {
        val folder = createFolder(appDirectory)
            File(folder, userFileName)
            File(folder, constructorFileName)
        val json = Helper.converting(context.assets.open(userFileName))
        writeFile(userFileName, json)
        return User.createUser(json)
    }

    fun signInQuestionnaire(questionnaire: Questionnaire){

    }

    fun signInUser(){

    }

    private fun getFile(fileName: String) =
        try {
            val root = Environment.getExternalStorageDirectory()
            val folder = File(root, appDirectory)
            File(folder, fileName)
        }
        catch (ex: Exception) {
            Log.e("ExceptionGettingFile", ex.toString())
            null
        }

    fun createFolder(name: String): File {
        val baseDir: File? = Environment.getExternalStorageDirectory()

        val folder = File(baseDir, name)
        if (folder.exists())
            return folder
        if (folder.isFile)
            folder.delete()
        return if (folder.mkdirs()) {
            folder
        } else Environment.getExternalStorageDirectory()
    }

    fun writeFile(fileName: String, string: String){
        val file = getFile(fileName)
        if (file != null) {
            val bufferedWriter = BufferedWriter(FileWriter(file))
                bufferedWriter.write(string)
                bufferedWriter.close()
        }
    }

    fun readFile(fileName: String): String {
        val file = getFile(fileName)
        return if (file != null)
            Helper.converting(FileInputStream(file))
        else ""
    }

}