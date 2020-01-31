package com.ui.event

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
import com.ui.custom.CustomModalWindow
import com.ui.MainActivity
import com.R
import com.logic.Executor
import com.json.questionnaire.Questionnaire
import com.json.user.ObResult
import com.openSource
import java.io.File


class PresentativeQuestionnaire : Fragment() {

    lateinit var xEngine: Executor
    lateinit var activity: AppCompatActivity
    lateinit var questionnaire: Questionnaire
    lateinit var obResult: ObResult
    var laterObResult: ObResult? = null

    lateinit var fragment: QuestionSession

    private var checkSingleLine = true
    private var isCompleted = false
    private lateinit var titleView: TextView
    private lateinit var fabStart: FloatingActionButton
    private lateinit var fabExit: FloatingActionButton
    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var imagePresents: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var laterResult: TextView
    private lateinit var sourceLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        views = inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)

        views.apply {
            activity = context as AppCompatActivity
            titleView = findViewById(R.id.Questionnaire_Title)
            fabStart = findViewById(R.id.Questionnaire_FABStart)
            fabExit = findViewById(R.id.Questionnaire_FABExit)
            toolbar = findViewById(R.id.Questionnaire_Toolbar)
            imagePresents = findViewById(R.id.Questionnaire_Image)
            descriptionView = findViewById(R.id.Questionnaire_Description)
            laterResult = findViewById(R.id.Questionnaire_LaterResult)
            sourceLayout = findViewById(R.id.Questionnaire_SourseLayout)
        }

        laterResult.setOnClickListener {
            if (laterObResult is ObResult)
                openAnalytics(laterObResult!!)
            else Toast.makeText(context, "Пока нет результатов", Toast.LENGTH_SHORT).show()
        }

        titleView.setOnClickListener {
            if (it is TextView) {
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
                sourceLayout.addView(
                    TextView(context).apply {
                        setTextColor(Color.parseColor("#6295C3"))
                        text = path.path
                        setOnClickListener {
                            when {
                                (source is String) -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source)))
                                (source is File) -> {
                                    try {
                                        val intent = Intent(Intent.EXTRA_TEXT, Uri.parse(source.absolutePath))
                                        //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(source.absoluteFile))
                                        startActivity(intent)
                                    }
                                    catch (ex: Exception) {
                                        Log.e("ex", ex.toString())
                                    }
                                }
                            }
                        }
                    })
            }

            if (resources.isEmpty()) sourceLayout.addView(TextView(context).apply { text = "Нет ресурсов" })

            fabStart.setOnClickListener {
                xEngine = Executor()
                xEngine.createEvent(questionnaire, laterObResult)
                xEngine.setOnActionEndEvent {
                    val fragment = CustomModalWindow().apply {
                        setTitle = "Завершить тестирование!"
                        setDescription = "Вы хотите завершить анкету и узнать свой результат?"
                        action = {
                            addButtonAction("Cancel", CustomModalWindow.BUTTON_CANCEL) {
                                dismiss()
                            }
                            addButtonAction("Ok", CustomModalWindow.BUTTON_OK){
                                openAnalytics(obResult)
                                dismiss()
                            }
                        }
                    }
                    fragment.show(activity.supportFragmentManager, javaClass.name)
                }
                nextQuestion()
            }

            fabExit.setOnClickListener {
                try {
                    val intent = Intent(context, MainActivity::class.java)
                    if (isCompleted){
                        val ob = obResult.toJsonObject()
                        Log.e("ob", ob)
                        intent.putExtra("obResult", ob)
                        intent.putExtra("type", "obResult")
                    }
                    activity.startActivity(intent)
                }
                catch (ex: Exception){
                    Log.e("exObResult", ex.toString())
                }
            }
        }
        return views
    }

    private fun openAnalytics(obResult: ObResult){
        val fragment = AnalyticsFragment()
        fragment.obResult = obResult
        fragment.contextQuestionnaire = this@PresentativeQuestionnaire
        activity.supportFragmentManager?.beginTransaction()?.replace(R.id.MainQuestionnaireLayout, fragment)?.commit()
    }

    fun nextQuestion() {
        try {
            if (xEngine.isInLast)
                obResult = xEngine.endEvent()
            else {
                xEngine.next()
                val fragment = QuestionSession().apply {
                    question = xEngine.question
                    contextQuestion = this@PresentativeQuestionnaire
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            }
        } catch (ex: Exception) {
            Log.e("ex", ex.toString())
        }
    }

    fun backQuestion() {
        try {
            val fragment: Fragment = if (xEngine.isInFirst)
                this
            else {
                xEngine.back()
                QuestionSession().apply {
                    question = xEngine.question
                    contextQuestion = this@PresentativeQuestionnaire
                }
            }
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
        } catch (ex: Exception) {
            Log.e("ex", ex.toString())
        }
    }
}