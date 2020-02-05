package com.ui


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import com.ui.game.Subject
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import com.*
import com.logic.IOManager
import com.ui.game.SubjectHolder
import com.ui.custom.CustomAdapter


class GameOfflineSessions : Fragment() {

    lateinit var activity: MainActivity
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
            val customAdapter = CustomAdapter<Subject, SubjectHolder>(R.layout.subject_card_view)
                customAdapter.activity = activity
                customAdapter.group = create()
                customAdapter.onBindLambda = { holder, item ->
                    holder.activity = activity
                    holder.downloadResources(item, IOManager.user.game.arrayList[customAdapter.group.indexOf(item)])
                }
                customAdapter.returnedClass = { inflater, parent ->
                    val view = inflater.inflate(customAdapter.layout, parent, false)
                    SubjectHolder(view)
                }
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = customAdapter
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
                    add(
                        Subject(
                            item["subject"].toString(),
                            item["background"].toString(),
                            item["path"].toString()
                        )
                    )
                }
            }
            catch (ex: Exception) {
                Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }
}
