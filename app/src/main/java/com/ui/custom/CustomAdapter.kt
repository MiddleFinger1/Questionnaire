package com.ui.custom

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.ViewGroup


class CustomAdapter<T, Holder: ViewHolder>(val layout: Int): Adapter<Holder>() {

    lateinit var onBindLambda: (holder: Holder, item: T) -> Unit
    lateinit var returnedClass: (inflater: LayoutInflater, parent: ViewGroup) -> Holder
    lateinit var activity: AppCompatActivity
    lateinit var group: ArrayList<T>

    override fun getItemCount() = group.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        onBindLambda(holder, group[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return returnedClass(layoutInflater, parent)
    }
}