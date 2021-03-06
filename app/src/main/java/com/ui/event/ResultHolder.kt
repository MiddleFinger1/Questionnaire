package com.ui.event

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.R
import com.json.user.ItemResult

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
            if (itemResult.truth) {
                truthBooleanView.setTextColor(Color.parseColor("#008577"))
                "Ответ правильный"
            }
            else {
                truthBooleanView.setTextColor(Color.parseColor("#C75450"))
                "Ответ неправильный"
            }
        if (showTruth && !itemResult.truth)
            truthView.text = itemResult.thuthAnswer.toString()
    }
}