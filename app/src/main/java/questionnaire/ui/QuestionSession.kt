package questionnaire.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import application.R
import kotlinx.android.synthetic.main.layout_game_offline_sessions.view.*
import questionnaire.Question
import questionnaire.Statements


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var question: Question
    lateinit var contextQuestion: PresentativeQuestionnaire

    private var answer = ""

    private lateinit var views: View
    private lateinit var buttonNext: Button
    private lateinit var buttonExit: Button
    private lateinit var buttonBack: Button
    private lateinit var questionTitle: TextView
    private lateinit var descriptionView: TextView
    private lateinit var questionImage: ImageView
    private lateinit var statementsLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        views = inflater.inflate(R.layout.fragment_question_layout, container, false)

        views.apply {
            buttonExit = findViewById(R.id.Question_Exit)
            buttonNext = findViewById(R.id.Question_Next)
            buttonBack = findViewById(R.id.Question_Back)
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
            if (answer != "") contextQuestion.nextQuestion()
        }

        buttonBack.setOnClickListener {
            contextQuestion.backQuestion()
        }

        buttonExit.setOnClickListener {
            contextQuestion.activity.supportFragmentManager.beginTransaction().replace(R.id.MainQuestionnaireLayout, contextQuestion).commit()
        }

        questionTitle.text = question.question
        descriptionView.text = question.decription

        createChoice(statementsLayout, question.statements)

        // statements and image soon must be add
        return views
    }

    private fun createChoice(layout: LinearLayout, statements: Statements){

        when (statements.type){

            Statements.SINGLE -> {

                val groupButton = RadioGroup(context)
                for (item in statements)
                    groupButton.addView(RadioButton(context).apply {
                        text = item
                        setOnClickListener {
                            if (!it.isActivated) answer = text.toString()
                        }
                    })
                layout.addView(groupButton)
            }
            Statements.MULTI ->
                for (item in statements)
                    layout.addView(RadioButton(context).apply {
                        text = item
                    })
            Statements.ENTER -> {
                layout.addView(EditText(context).apply {

                })
            }
        }
    }
}
