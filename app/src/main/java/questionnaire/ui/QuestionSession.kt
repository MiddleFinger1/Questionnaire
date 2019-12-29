package questionnaire.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import application.R
import questionnaire.Question


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var question: Question
    lateinit var contextQuestion: PresentativeQuestionnaire

    private lateinit var views: View
    private lateinit var buttonNext: Button
    private lateinit var buttonExit: Button
    private lateinit var questionTitle: TextView
    private lateinit var descriptionView: TextView
    private lateinit var questionImage: ImageView
    private lateinit var statementsLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        views = inflater.inflate(R.layout.fragment_question_layout, container, false)

        views.apply {
            buttonExit = findViewById(R.id.Question_Exit)
            buttonNext = findViewById(R.id.Question_Next)
            questionTitle = findViewById(R.id.Question_Question)
            descriptionView = findViewById(R.id.Question_Description)
            questionImage = findViewById(R.id.Question_Image)
            statementsLayout = findViewById(R.id.Question_Statements)
        }

        buttonNext.text =
            if (contextQuestion.scene == question.context.lastIndex)
                resources.getString(R.string.finish_questionnaire)
            else resources.getString(R.string.next_question)

        buttonNext.setOnClickListener {
            // if
            try {
                contextQuestion.nextQuestion()
            }
            catch (ex: Exception){
                Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
            }
        }

        buttonExit.setOnClickListener {
            contextQuestion.activity.supportFragmentManager.beginTransaction().replace(R.id.MainLayout, contextQuestion).commit()
        }

        questionTitle.text = question.question
        descriptionView.text = question.decription

        // statements and image soon must be add
        return views
    }




}
