package com.questionnaire.game

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import com.Helper
import com.MainActivity
import com.R
import com.databinding.SubjectCardViewBinding
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import com.questionnaire.Settings
import com.questionnaire.ui.QuestionnaireGroup


class SubjectHolder(val view: View): ViewHolder(view){

    lateinit var activity: MainActivity
    private lateinit var binding: SubjectCardViewBinding

    fun downloadResources(subject: Subject){
        try {
            val input = activity.assets.open(subject.background)
            val drawable = Drawable.createFromStream(input, null)
            binding = SubjectCardViewBinding.bind(view)
            binding.subject = subject
            binding.background = drawable
            view.setOnClickListener{
                val json = Helper.converting(activity.assets.open(subject.path))
                val jsonObject = JSONParser().parse(json) as JSONObject
                val jsonArray = jsonObject["settings"] as JSONArray
                val settings = arrayListOf<Settings>()
                for (item in jsonArray)
                    settings.add(Settings.createSettings(item.toString()))
                val fragment = QuestionnaireGroup()
                    fragment.activity = activity
                    fragment.settings = settings
                    activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, fragment).commit()
            }
        }
        catch (ex: Exception) {
            Log.e("subjectEx", ex.toString())
        }
    }
}