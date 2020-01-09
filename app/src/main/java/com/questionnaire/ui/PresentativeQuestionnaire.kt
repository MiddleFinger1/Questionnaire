package com.questionnaire.ui


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.application.MainActivity
import com.application.R
import com.questionnaire.Questionnaire


class PresentativeQuestionnaire : Fragment() {

    lateinit var activity: AppCompatActivity
    lateinit var questionnaire: Questionnaire
    private var checkSingleLine = true

    private lateinit var titleView: TextView
    private lateinit var fabStart: FloatingActionButton
    private lateinit var fabExit: FloatingActionButton
    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var imagePresents: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var sourceLayout: LinearLayout

    var scene = -1
    var points = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)

        views.apply {
            titleView = findViewById(R.id.Questionnaire_Title)
            fabStart = findViewById(R.id.Questionnaire_FABStart)
            fabExit = findViewById(R.id.Questionnaire_FABExit)
            toolbar = findViewById(R.id.Questionnaire_Toolbar)
            imagePresents = findViewById(R.id.Questionnaire_Image)
            descriptionView = findViewById(R.id.Questionnaire_Description)
            sourceLayout = findViewById(R.id.Questionnaire_SourseLayout)
        }

        titleView.setOnClickListener {
            if (it is TextView){
                checkSingleLine = !checkSingleLine
                it.setSingleLine(checkSingleLine)
            }
        }

        questionnaire.apply {
            titleView.text = settings.title
            descriptionView.text = description

            if (settings.icon != null) {
                val icon = openSource(activity, settings.icon!!)
                if (icon is Drawable)
                    imagePresents.background = icon
            }
            for (path in resources) {
                val source = openSource(activity, path)
                sourceLayout.addView(when {
                    (source is String) -> TextView(context).apply {
                        setTextColor(Color.parseColor("#6295C3"))
                        text = path.path
                        setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source)))
                        }
                    }
                    else -> null
                })
            }

            if (resources.isEmpty()) sourceLayout.addView(TextView(context).apply { text = "Нет ресурсов" })

            fabStart.setOnClickListener {
                scene = -1
                nextQuestion()
            }

            fabExit.setOnClickListener {
                activity.startActivity(Intent(context, MainActivity::class.java))
            }
        }
        return views
    }

    fun nextQuestion() {
        try {
            if (scene < questionnaire.lastIndex) {
                scene += 1
                val fragment = QuestionSession().apply {
                    question = questionnaire[scene]
                    contextQuestion = this@PresentativeQuestionnaire
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            }
            else {
                val fragment = this
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            }
        }
        catch (ex: Exception){
            Log.e("ex", ex.toString())
        }
    }

    fun backQuestion(){
        try {
            val fragment: Fragment
            if (scene > 0) {
                scene -= 1
                fragment = QuestionSession()
                fragment.question = questionnaire[scene]
                fragment.contextQuestion = this
                points -= questionnaire[scene].cost
            } else fragment = this
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
