package com.questionnaire.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.application.R
import com.questionnaire.Settings


class SettingsAdapter: Adapter<ViewHolder>() {

    lateinit var activity: AppCompatActivity
    lateinit var groupSettings: ArrayList<Settings>
    private lateinit var layoutInflater: LayoutInflater

    override fun getItemCount() = groupSettings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is SettingsHolder) {
            val settings = groupSettings[position]
            holder.activity = activity
            holder.downloadSettings(settings)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.settings_card_view, parent, false)
        return SettingsHolder(view)
    }


}