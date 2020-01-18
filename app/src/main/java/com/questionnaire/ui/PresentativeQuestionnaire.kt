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
import com.MainActivity
import com.application.R
import com.questionnaire.Questionnaire
import com.users.ObResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class PresentativeQuestionnaire : Fragment() {

    lateinit var activity: AppCompatActivity
    lateinit var questionnaire: Questionnaire
    lateinit var obResult: ObResult

    private var checkSingleLine = true
    private val idQuestions = arrayListOf<Int>()
    private lateinit var titleView: TextView
    private lateinit var fabStart: FloatingActionButton
    private lateinit var fabExit: FloatingActionButton
    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var imagePresents: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var sourceLayout: LinearLayout
    var sceneInstance = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        obResult = ObResult().apply {
            isPresentedTruth = questionnaire.settings.isPresented
        }

        CoroutineScope(Dispatchers.Main).launch {
            setArray()
        }

        views = inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)

        views.apply {
            activity = context as AppCompatActivity

            titleView = findViewById(R.id.Questionnaire_Title)
            fabStart = findViewById(R.id.Questionnaire_FABStart)
            fabExit = findViewById(R.id.Questionnaire_FABExit)
            toolbar = findViewById(R.id.Questionnaire_Toolbar)
            imagePresents = findViewById(R.id.Questionnaire_Image)
            descriptionView = findViewById(R.id.Questionnaire_Description)
            sourceLayout = findViewById(R.id.Questionnaire_SourseLayout)
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
                sceneInstance = -1
                obResult = ObResult()
                obResult.isPresentedTruth = questionnaire.settings.isPresented
                nextQuestion()
            }

            fabExit.setOnClickListener {
                try {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("obResult", obResult.toString())
                    activity.startActivity(intent)
                }
                catch (ex: Exception){
                    Log.e("ex", ex.toString())
                }
            }
        }
        return views
    }

    private fun setArray(){
        if (questionnaire.isRandom){
            val range = arrayListOf<Int>()
            range.addAll(0.until(questionnaire.size))
            while (idQuestions.size != questionnaire.maxQuestions){
                val int = Random(System.currentTimeMillis()).nextInt(range.size)
                idQuestions.add(range[int])
                range.removeAt(int)
            }
        }
        else idQuestions.addAll(0..questionnaire.lastIndex)
        Log.e("ex", idQuestions.toString())
    }

    fun nextQuestion() {
        try {
            if (sceneInstance < idQuestions.lastIndex) {
                sceneInstance += 1
                val fragment = QuestionSession().apply {
                    question = questionnaire[idQuestions[sceneInstance]]
                    contextQuestion = this@PresentativeQuestionnaire
                }
                activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
            } else {
                val fragment = DialogAlert()
                fragment.contextQuestionnaire = this
                fragment.show(activity.supportFragmentManager, fragment.javaClass.name)
            }
        } catch (ex: Exception) {
            Log.e("ex", ex.toString())
        }
    }

    fun backQuestion() {
        try {
            val fragment: Fragment
            if (sceneInstance > 0) {
                sceneInstance -= 1
                fragment = QuestionSession()
                fragment.question = questionnaire[idQuestions[sceneInstance]]
                fragment.contextQuestion = this
                obResult.cost -= questionnaire[idQuestions[sceneInstance]].cost
            } else fragment = this
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment)
                .commit()
        } catch (ex: Exception) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}