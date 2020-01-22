package com

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class CustomAdapter<T, Holder: ViewHolder>(val layout: Int): Adapter<Holder>() {

    lateinit var onBindLambda: (holder: Holder, item: T) -> Unit
    lateinit var returnedClass: (view: View) -> Holder
    lateinit var activity: AppCompatActivity
    lateinit var group: ArrayList<T>

    override fun getItemCount() = group.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onBindLambda(holder, group[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(layout, parent, false)
        return returnedClass(view)
    }
}