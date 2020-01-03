package questionnaire.ui


import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import application.MainActivity
import application.R
import questionnaire.Questionnaire
import questionnaire.Source
import questionnaire.ui.openSourse


class PresentativeQuestionnaire : Fragment() {

    lateinit var activity: AppCompatActivity
    lateinit var questionnaire: Questionnaire

    private lateinit var titleView: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var imagePresents: ImageView
    private lateinit var descriptionView: TextView
    private lateinit var sourceLayout: LinearLayout
    private lateinit var buttonExit: Button

    var scene = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)

        views.apply {
            titleView = findViewById(R.id.Questionnaire_Title)
            fab = findViewById(R.id.Questionnaire_FABStart)
            toolbar = findViewById(R.id.Questionnaire_Toolbar)
            imagePresents = findViewById(R.id.Questionnaire_Image)
            descriptionView = findViewById(R.id.Questionnaire_Description)
            buttonExit = findViewById(R.id.Questionnaire_Cancel)
            sourceLayout = findViewById(R.id.Questionnaire_SourseLayout)
        }

        questionnaire.apply {
            titleView.text = settings.title
            descriptionView.text = description

            if (settings.icon != null) {
                val icon = openSourse(activity, settings.icon!!)
                if (icon is Drawable)
                    imagePresents.background = icon
            }
            for (path in resources) {
                val source = openSourse(activity, path)

                sourceLayout.addView(when {
                    (source is String) -> TextView(context).apply {
                        text = path.path
                        setOnClickListener {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(source)))
                        }
                    }
                    else -> null
                })
            }

            if (resources.isEmpty()) sourceLayout.addView(TextView(context).apply { text = "Нет ресурсов" })

            fab.setOnClickListener {
                scene = -1
                nextQuestion()
            }
        }

        buttonExit.setOnClickListener {
            activity.startActivity(Intent(context, MainActivity::class.java))
        }

        return views
    }

    fun nextQuestion() {
        try {
            val fragment: Fragment
            if (scene < questionnaire.lastIndex) {
                scene += 1
                fragment = QuestionSession()
                fragment.question = questionnaire[scene]
                fragment.contextQuestion = this
            } else fragment = this
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
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
            } else fragment = this
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, fragment).commit()
        }
        catch (ex: Exception){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
