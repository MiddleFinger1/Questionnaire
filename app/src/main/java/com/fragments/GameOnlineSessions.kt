package com.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Helper
import com.MEngine
import com.MainActivity
import com.R
import com.questionnaire.constructor.ConstructorActivity
import java.io.File
import java.io.FileInputStream


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
            try {
                val json = MEngine.readFile(MEngine.constructorFileName)
                val intent = Intent(context, ConstructorActivity::class.java)
                intent.putExtra("questionnaire", json)
                startActivity(intent)
            }
            catch (ex: Exception) {
                Log.e("openConstructor", ex.toString())
            }
        }

        return views
    }

}
