package com.fragments


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.CustomModalWindow
import com.MEngine
import com.MainActivity
import com.R
import com.questionnaire.ui.openSource


class HomeSettings : Fragment() {

    lateinit var activity: MainActivity
    lateinit var views: View

    private lateinit var logoView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var userIdView: TextView
    private lateinit var fabFindUsers: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_home_settings, container, false)
        views.apply {
            logoView = findViewById(R.id.USERS_Logotype)
            toolbar = findViewById(R.id.USERS_Toolbar)
            userIdView = findViewById(R.id.USERS_IdView)
            fabFindUsers = findViewById(R.id.USERS_fabFindUsers)
        }
        val user = MEngine.user
        val icon = openSource(activity, user.settings.icon)
        if (icon is Drawable)
            logoView.background = icon
        toolbar.title = user.settings.login
        userIdView.text = user.settings.userID.toString()

        fabFindUsers.setOnClickListener {
            val fragment = CustomModalWindow()
                fragment.setTitle = "WARNING!!"
                fragment.setDescription = "IT IS MODAL WINDOW!"
                fragment.action = {
                    addButtonAction("Ok") {
                        dismiss()
                    }
                }
                fragment.show(activity.supportFragmentManager, fragment.javaClass.name)
        }
        return views
    }
}
