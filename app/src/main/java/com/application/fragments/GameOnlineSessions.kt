package com.application.fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.R


class GameOnlineSessions : Fragment() {

    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.layout_game_online_sessions, container, false)

        views.apply {
            toolbar = findViewById(R.id.onLine_Toolbar)

        }


        return views
    }

}
