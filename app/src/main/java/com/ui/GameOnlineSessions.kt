package com.ui

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.logic.IOManager
import com.R
import com.ui.constructor.ConstructorActivity


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
                val json = IOManager.readFile(IOManager.constructorFileName)
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
