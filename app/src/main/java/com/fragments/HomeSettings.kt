package com.fragments


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.MainActivity
import com.R
import com.questionnaire.ui.openSource


class HomeSettings : Fragment() {

    lateinit var activity: MainActivity
    lateinit var views: View

    private lateinit var logoView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var userIdView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_home_settings, container, false)
        views.apply {
            logoView = findViewById(R.id.USERS_Logotype)
            toolbar = findViewById(R.id.USERS_Toolbar)
            userIdView = findViewById(R.id.USERS_IdView)
        }
        val user = activity.user
        val icon = openSource(activity, user.settings.icon)
        if (icon is Drawable)
            logoView.background = icon
        toolbar.title = user.settings.login
        userIdView.text = user.settings.userID.toString()
        return views
    }
}
