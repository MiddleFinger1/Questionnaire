package com

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class CustomAdapter<T>(
    val layout: Int

): Adapter<ViewHolder>() {

    lateinit var onBindLambda: (holder: ViewHolder, item: T) -> Unit
    lateinit var returnedClass: (view: View) -> ViewHolder
    lateinit var activity: AppCompatActivity
    lateinit var group: ArrayList<T>

    override fun getItemCount() = group.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindLambda(holder, group[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layout, parent, false)
        return returnedClass(view)
    }
}