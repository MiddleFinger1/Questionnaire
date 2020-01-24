package com.questionnaire.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.CustomAdapter
import com.MainActivity
import com.R
import com.fragments.GameOfflineSessions
import com.questionnaire.Settings


class QuestionnaireGroup : Fragment() {

    lateinit var activity: MainActivity
    lateinit var settings: ArrayList<Settings>
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.fragment_questionnaire_group, container, false)

        views.apply {
            recyclerView = findViewById(R.id.Group_recyclerView)
            recyclerView.setHasFixedSize(true)

            toolbar = findViewById(R.id.Group_Toolbar)

            toolbar.setNavigationOnClickListener {
                val fragment = GameOfflineSessions()
                fragment.activity = activity
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            val customAdapter = CustomAdapter<Settings, SettingsHolder>(R.layout.settings_card_view)
                customAdapter.activity = activity
                customAdapter.group = settings
                customAdapter.onBindLambda = { holder, item ->
                    holder.activity = activity
                    holder.downloadSettings(item)
                }
                customAdapter.returnedClass = { inflater, parent ->
                    val view = inflater.inflate(customAdapter.layout, parent, false)
                    SettingsHolder(view)
                }

            //val adapter = SettingsAdapter()
            //adapter.activity = activity
            //adapter.groupSettings = settings

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter

        }
        return views
    }
}
