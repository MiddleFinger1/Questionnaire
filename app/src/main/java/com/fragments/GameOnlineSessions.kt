package com.fragments


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.R
import com.questionnaire.constructor.ConstructorActivity


class GameOnlineSessions : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.layout_game_online_sessions, container, false)

        views.apply {
            toolbar = findViewById(R.id.onLine_Toolbar)
            fab = findViewById(R.id.onLine_FABAdd)
        }

        fab.setOnClickListener {
            startActivity(Intent(context, ConstructorActivity::class.java))
        }

        return views
    }

}
