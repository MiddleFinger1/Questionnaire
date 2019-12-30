package questionnaire.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import application.R
import questionnaire.Questionnaire
import questionnaire.Statements


class PresentativeQuestionnaire : Fragment() {

    lateinit var activity: AppCompatActivity
    lateinit var questionnaire: Questionnaire
    private lateinit var views: View
    private lateinit var imagePresents: ImageView
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var buttonOk: Button
    var scene = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.layout_presentive_questionnaire, container, false)

        views.apply {
            imagePresents = findViewById(R.id.Questionnaire_Image)
            titleView = findViewById(R.id.Questionnaire_Title)
            descriptionView = findViewById(R.id.Questionnaire_Description)
            buttonOk = findViewById(R.id.Questionnaire_Ok)
        }

        questionnaire.apply {
            titleView.append(settings.title)
            descriptionView.append(description)

            buttonOk.setOnClickListener {
                scene = -1
                nextQuestion()
            }
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
