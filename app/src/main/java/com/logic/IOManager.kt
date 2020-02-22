package com.logic

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import com.Helper
import com.json.user.User
import java.io.*


object IOManager {

    lateinit var user: User

    const val userFileName = "user.json"
    const val constructorFileName = "constructor.json"
    const val dataFileName = "data.json"
    const val appDirectory = "Questionnaire"

    fun findFilesConfigs(): Boolean {

        val folderPath = "${Environment.getExternalStorageDirectory().absolutePath}/$appDirectory"
        val folder = File(folderPath)
        val userFile = File("$folderPath/$userFileName")
        val constFile = File("$folderPath/$constructorFileName")
        val dataFile = File("$folderPath/$dataFileName")

        return folder.exists() && userFile.exists() && constFile.exists() && dataFile.exists()
    }

    // получить доступ к файловой системе относительно версии ОС (6.0 и выше)
    fun getRulesOfFS(context: Context){
        if (android.os.Build.VERSION.SDK_INT >= 23){
            val check = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED
            if (check){
                Log.e("permision","Permission is granted")
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    fun downloadUser() {
        val json = readFile(userFileName)
        Log.e("jsonDownloadUser", json)
        val user = if (json != "")
            User.createUser(json)
        else null
        if (user != null)
            IOManager.user = user
    }

    fun createUserInstance(context: Context) {
        try {
            val folder = createFolder(appDirectory)
            val fileUser = File(folder, userFileName)
                Log.e("path", fileUser.absolutePath)
                fileUser.createNewFile()
            File(folder, constructorFileName).createNewFile()
            File(folder, dataFileName).createNewFile()
            val json = Helper.converting(context.assets.open(userFileName))
            writeFile(userFileName, json)
            downloadUser()
        }
        catch (ex: Exception) {
            Log.e("exCreatingUserInstance", ex.toString())
        }
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