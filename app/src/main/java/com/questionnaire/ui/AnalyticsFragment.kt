package com.questionnaire.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.application.R


class AnalyticsFragment : Fragment() {

    lateinit var contextQuestionnaire: PresentativeQuestionnaire
    private lateinit var textPointsGotten: TextView
    private lateinit var textMarksGotten: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.fragment_analytics, container, false)
        views.apply {
            textPointsGotten = findViewById(R.id.Analytics_PointsGotten)
            textMarksGotten = findViewById(R.id.Analytics_MarksGotten)
            toolbar = findViewById(R.id.Analytics_Toolbar)
        }
        toolbar.title = "Результаты"
        toolbar.setNavigationOnClickListener {
            contextQuestionnaire.activity.
                supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, contextQuestionnaire).commit()
        }

        val analytics = contextQuestionnaire.questionnaire.analytics
        textPointsGotten.text = contextQuestionnaire.points.toString()
        try {
            for (id in 0..analytics.marks.lastIndex)
                if (analytics.points[id] <= contextQuestionnaire.points) {
                    textMarksGotten.text = "you're gotten ${analytics.marks[id]}"
                    break
                }

        }
        catch (ex: Exception) {
            Log.e("ex", ex.toString())
        }
        return views
    }
}
