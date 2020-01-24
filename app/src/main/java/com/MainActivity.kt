package com

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.fragments.GameOfflineSessions
import com.fragments.GameOnlineSessions
import com.fragments.HomeSettings
import com.users.ObResult
import com.users.User
import java.io.*


class MainActivity : AppCompatActivity() {

    lateinit var user: User
    private lateinit var navView: BottomNavigationView

	private val rootDirectory = Environment.getExternalStorageDirectory().absolutePath
	private val appDirectory = "Questionnaire"
    private val userFileName = "user.json"
    private val fileInstanceUser = "$rootDirectory/$appDirectory/$userFileName"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setBackgroundDrawable(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#ACACAC")

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener {
            switchFragment(it.itemId)
            true
        }

        downloadUser()
        if (intent != null)
            getObResult(intent)
        switchFragment(R.id.navigation_home)
    }

    private fun create(name: String): File {
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

    private fun getObResult(data: Intent) {
        try {
            val json = data.getStringExtra("obResult")
            if (json != null){
                //Log.e("obResult", json)

                val obResult = ObResult.createObResult(json)

                if (obResult != null) {
                    Log.e("obResult", obResult.toJsonObject())
                    for (id in 0..user.analytics.lastIndex){
                        val item = user.analytics[id]
                        if (item.id == obResult.id)
                            user.analytics[id] = obResult
                    }
                    user.analytics.add(obResult)
                    Log.e("analytics", user.analytics.toJsonObject())
                }

                val jsonObject = user.toJsonObject()
                Log.e("jsonObject", user.toJsonObject())

                val root = Environment.getExternalStorageDirectory()
                val folder = File(root, appDirectory)
                val file = File(folder, userFileName)

                val bufferedWriter = BufferedWriter(FileWriter(file))
                bufferedWriter.write(jsonObject)
                bufferedWriter.close()

                val check = Helper.converting(FileInputStream(file))
                Log.e("check", check)
            }
        }
        catch (ex: Exception) {
            Log.e("exGetObResult", ex.toString())

        }
    }

    private fun downloadUser() {
        // загрузка пользователя производится из assets
        Log.e("path", fileInstanceUser)
        val user: User? = try {
            val file = File(fileInstanceUser)
            val input = FileInputStream(file)
            val json = Helper.converting(input)
            Log.e("json", json)
            val jsonUser = User.createUser(json)
            Log.e("jsonUser", jsonUser.toString())
            if (jsonUser != null)
                Log.e("jsonUser", jsonUser.toJsonObject())
            jsonUser
        } catch (ex: Exception) {
            Log.e("exDownloadUser", ex.toString())
            val folder = create(appDirectory)
            val file = File(folder, userFileName)
            val bufferedWriter = BufferedWriter(FileWriter(file))
            Log.e("instanceJson", "is empty")
            val inputStream = assets.open(userFileName)
            val json = Helper.converting(inputStream)
            bufferedWriter.write(json)
            bufferedWriter.close()
            User.createUser(json)
        }
        if (user != null) {
            this.user = user
            Log.e("finalUser", this.user.toJsonObject())
        }
    }

    private fun switchFragment(id: Int){
        val item = navView.menu.findItem(id)
        item.isChecked = true
        val fragment: Fragment? = when (item.itemId) {
            R.id.navigation_home ->
                HomeSettings().apply { activity = this@MainActivity}
            R.id.navigation_dashboard ->
                GameOfflineSessions().apply { activity = this@MainActivity}
            R.id.navigation_notifications -> {
                GameOnlineSessions()
            }
            else -> null
        }
        if (fragment != null)
            supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
    }
}
