package com.fragments


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.Helper
import com.application.R
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import com.questionnaire.game.Subject
import com.questionnaire.game.SubjectAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar


class GameOfflineSessions : Fragment() {

    lateinit var activity: AppCompatActivity
    private lateinit var views: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        views = inflater.inflate(R.layout.layout_game_offline_sessions, container, false)

        return views.apply {

            toolbar = findViewById(R.id.offLine_Toolbar)
            toolbar.title = "Прохождение анкет"
            toolbar.setTitleTextColor(Color.BLACK)

            recyclerView = findViewById(R.id.OfflineSessions_RecyclerView)
            recyclerView.setHasFixedSize(true)

            val adapter = SubjectAdapter()
            adapter.activity = activity
            adapter.subjects = create()

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
    }

    private fun create() =
        arrayListOf<Subject>().apply {
            try {
                val json = Helper.converting(resources.openRawResource(R.raw.subjects))
                val jsonObject = JSONParser().parse(json) as JSONObject
                val jsonArray = jsonObject["subjects"] as JSONArray
                for (item in jsonArray){
                    item as JSONObject
                    add(Subject(
                        item["subject"].toString(),
                        item["background"].toString(),
                        item["path"].toString()
                    ))
                }
            }
            catch (ex: Exception) {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }
}
