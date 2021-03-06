package com.ui


import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ui.custom.CustomModalWindow
import com.logic.IOManager
import com.R
import com.openSource


class HomeSettings : Fragment() {

    lateinit var activity: MainActivity
    lateinit var views: View

    private lateinit var logoView: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var fabFindUsers: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_home_settings, container, false)
        views.apply {
            logoView = findViewById(R.id.USERS_Logotype)
            toolbar = findViewById(R.id.USERS_Toolbar)
            fabFindUsers = findViewById(R.id.USERS_fabFindUsers)
        }
        val user = IOManager.user
        val icon = openSource(activity, user.settings.icon)
        if (icon is Drawable)
            logoView.background = icon
        toolbar.title = user.settings.login

        fabFindUsers.setOnClickListener {
            val fragment = CustomModalWindow()
                fragment.setTitle = "WARNING!!"
                fragment.setDescription = "IT IS MODAL WINDOW!"
                fragment.action = {
                    addButtonAction("Ok", CustomModalWindow.BUTTON_OK) {
                        dismiss()
                    }
                }
                fragment.show(activity.supportFragmentManager, fragment.javaClass.name)
        }
        return views
    }
}
