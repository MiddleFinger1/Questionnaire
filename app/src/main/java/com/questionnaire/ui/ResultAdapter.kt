package com.questionnaire.ui

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import com.application.R
import com.users.ObResult


class ResultAdapter: Adapter<ViewHolder>() {

    lateinit var activity: AppCompatActivity
    lateinit var groupResults: ObResult
    private lateinit var layoutInflater: LayoutInflater

    override fun getItemCount() = groupResults.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ResultHolder) {
            val result = groupResults[position]
            holder.downloadResult(result, groupResults.isPresentedTruth)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.result_layout_cardview, parent, false)
        return ResultHolder(view)
    }

}