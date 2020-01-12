package com.questionnaire.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.application.R
import com.users.ObResult.ItemResult

class ResultHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val questionView: TextView
    private val answerView: TextView
    private val truthBooleanView: TextView
    private val truthView: TextView

    init {
        view.apply {
            questionView = findViewById(R.id.Result_Question)
            answerView = findViewById(R.id.Result_Answer)
            truthBooleanView = findViewById(R.id.Result_TruthBoolean)
            truthView = findViewById(R.id.Result_Truth)
        }
    }

    fun downloadResult(itemResult: ItemResult, showTruth: Boolean){
        questionView.text = itemResult.question
        answerView.text = itemResult.answer
        truthBooleanView.text =
            if (itemResult.answer == itemResult.truth)
                "Ответ правильный"
            else "Ответ неправильный"
        if (showTruth)
            truthView.text = itemResult.truth
    }



}