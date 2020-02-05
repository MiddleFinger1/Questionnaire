package com.ui.game

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.Helper
import com.ui.MainActivity
import com.R
import com.databinding.SubjectCardViewBinding
import org.json.simple.JSONObject
import org.json.simple.JSONArray
import org.json.simple.parser.JSONParser
import com.json.questionnaire.Settings
import com.ui.event.QuestionnaireGroup


class SubjectHolder(val view: View): ViewHolder(view){

    lateinit var activity: MainActivity
    private lateinit var binding: SubjectCardViewBinding

    fun downloadResources(subject: Subject, array: ArrayList<Boolean>){
        try {
            val input = activity.assets.open(subject.background)
            val drawable = Drawable.createFromStream(input, null)
            binding = SubjectCardViewBinding.bind(view)
            binding.subject = subject
            binding.background = drawable
            binding.costKeys = "1"
            var count = 0
            for (item in array)
                if (item) count += 1
            binding.count = "Пройдено: $count/10"
            view.setOnClickListener{
                if (!array[0]) {
                    Toast.makeText(activity.baseContext, "Недоступен", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val json = Helper.converting(activity.assets.open(subject.path))
                val jsonObject = JSONParser().parse(json) as JSONObject
                val jsonArray = jsonObject["settings"] as JSONArray
                val settings = arrayListOf<Settings>()
                for (item in jsonArray)
                    settings.add(Settings.createSettings(item.toString()))
                val fragment = QuestionnaireGroup()
                    fragment.arrayComplete.addAll(array)
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