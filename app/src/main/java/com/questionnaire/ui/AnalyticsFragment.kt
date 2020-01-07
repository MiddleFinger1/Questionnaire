package com.questionnaire.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.application.R


class AnalyticsFragment : Fragment() {

    lateinit var contextQuestionnaire: PresentativeQuestionnaire
    lateinit var textPointsGotten: TextView
    lateinit var textMarksGotten: TextView
    lateinit var buttonExit: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.fragment_analytics, container, false)
        views.apply {
            textPointsGotten = findViewById(R.id.Analytics_PointsGotten)
            textMarksGotten = findViewById(R.id.Analytics_MarksGotten)
            buttonExit = findViewById(R.id.Analytics_Exit)
        }

        val analytics = contextQuestionnaire.questionnaire.analytics

        textPointsGotten.text = contextQuestionnaire.points.toString()

        for (got in analytics.marks.keys){
            if (got >= contextQuestionnaire.points)
                textMarksGotten.text = "you're gotten ${analytics.marks[got]}"
        }

        buttonExit.setOnClickListener {
            contextQuestionnaire.activity.
                supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, contextQuestionnaire).commit()
        }

        return views
    }


}
