package com.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ui.custom.CustomAdapter
import com.R
import com.json.user.ItemResult
import com.json.user.ObResult


class AnalyticsFragment : Fragment() {

    lateinit var obResult: ObResult
    lateinit var contextQuestionnaire: PresentativeQuestionnaire
    private lateinit var textPointsGotten: TextView
    private lateinit var textMarksGotten: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val views = inflater.inflate(R.layout.fragment_analytics, container, false)
        views.apply {
            textPointsGotten = findViewById(R.id.Analytics_PointsGotten)
            textMarksGotten = findViewById(R.id.Analytics_MarksGotten)
            toolbar = findViewById(R.id.Analytics_Toolbar)
            recyclerView = findViewById(R.id.Analytics_RecyclerView)
        }
        toolbar.setNavigationOnClickListener {
            contextQuestionnaire.laterObResult = obResult
            contextQuestionnaire.activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, contextQuestionnaire).commit()
        }
        val analytics = contextQuestionnaire.questionnaire.analytics
        textPointsGotten.text = obResult.cost.toString()
        try {
            for (id in 0..analytics.marks.lastIndex)
                if (analytics.points[id] <= obResult.cost) {
                    textMarksGotten.text = "you're gotten ${analytics.marks[id]}"
                    break
                }
        }
        catch (ex: Exception) {
            Log.e("ex", ex.toString())
        }
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        val customAdapter = CustomAdapter<ItemResult, ResultHolder>(R.layout.result_layout_cardview)
            customAdapter.group = obResult
            customAdapter.onBindLambda = { holder, item ->
                holder.downloadResult(item, obResult.isPresentedTruth)
            }
            customAdapter.returnedClass = { inFlater, parent ->
                val view = inFlater.inflate(customAdapter.layout, parent, false)
                ResultHolder(view)
            }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = customAdapter

        return views
    }
}
