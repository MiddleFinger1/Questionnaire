package com.ui.start

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import com.Helper
import com.R
import com.json.user.Settings
import com.json.user.User
import com.logic.Firing
import com.logic.IOManager
import com.ui.MainActivity
import java.io.FileInputStream


class BackFragment : Fragment() {

    private lateinit var buttonEnter: Button
    private lateinit var buttonExit: Button
    private lateinit var iconLaunch: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_back, container, false)
        views.apply {
            buttonEnter = findViewById(R.id.Start_EnterToApp)
            buttonExit = findViewById(R.id.Start_ExitFromAcc)
            iconLaunch = findViewById(R.id.Start_IconLaunch)
        }

        val appearImage = AnimationUtils.loadAnimation(context, R.anim.appear_icon)

        iconLaunch.startAnimation(appearImage)
        buttonEnter.setOnClickListener {

            val data = IOManager.readFile(IOManager.dataFileName)
            Log.e("data", data)
            if (data != "")
                getUserFromFireBase(data)
            else IOManager.downloadUser()
            startActivity(Intent(context, MainActivity::class.java))
        }
        buttonExit.animation = null
        buttonExit.setOnClickListener {
            val fragment = AuthFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }
        return views
    }

    private fun getUserFromFireBase(data: String){
        val settings = Settings.createSettings(data)
        Log.e("settings", settings.toString())

        if (settings != null){
            Log.e("settings", settings.toJsonObject())

            Firing.getFile(settings.path){ file, it ->
                val json = Helper.converting(FileInputStream(file))

                Log.e("status", it.bytesTransferred.toString())

                Log.e("jsonFile", json)
                val user = User.createUser(json)
                if (user != null) {
                    user.settings = settings
                    IOManager.user = user
                    Log.e("userFromFireBase", user.toJsonObject())
                }
            }
        }
    }

}
