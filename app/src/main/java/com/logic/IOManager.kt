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
        val json =
            try {
                readFile(userFileName)
            }
            catch(ex: Exception){
                Log.e("exReadFile", ex.toString())
                null
            }
        Log.e("jsonDownloadUser", json.toString())
        val user = if (json != null && json.toString().isNotEmpty())
            User.createUser(json)
        else createUser(context)
        if (user != null)
            IOManager.user = user
    }

    private fun findFolder(){

    }

    private fun createUser(context: Context): User? {
        /*
        File fileName = null;
String sdState = android.os.Environment.getExternalStorageState();
if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
File sdDir = android.os.Environment.getExternalStorageDirectory();
fileName = new File(sdDir, "cache/primer.txt");
} else {
fileName = context.getCacheDir();
}
if (!fileName.exists())
fileName.mkdirs();
try {
FileWriter f = new FileWriter(fileName);
f.write("hello world");
f.flush();
f.close();
} catch (Exception e) {

}
         */

        val folder = createFolder(appDirectory)
        val fileUser = File(folder, userFileName)
        val fileConst = File(folder, constructorFileName)

        FileWriter(fileUser)
        FileWriter(fileConst)

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