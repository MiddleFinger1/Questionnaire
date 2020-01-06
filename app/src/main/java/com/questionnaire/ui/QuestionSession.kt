package com.questionnaire.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.support.v7.widget.Toolbar
import com.application.R
import com.questionnaire.Question
import com.questionnaire.Statements


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var question: Question
    lateinit var contextQuestion: PresentativeQuestionnaire

    private var answer = ""

    private lateinit var views: View
    private lateinit var toolbar: Toolbar
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
            toolbar = findViewById(R.id.Question_Toolbar)
            buttonExit = findViewById(R.id.Question_Exit)
            buttonNext = findViewById(R.id.Question_Next)
            buttonBack = findViewById(R.id.Question_Back)
            questionTitle = findViewById(R.id.Question_Question)
            descriptionView = findViewById(R.id.Question_Description)
            questionImage = findViewById(R.id.Question_Image)
            statementsLayout = findViewById(R.id.Question_Statements)
        }

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.title = "${contextQuestion.scene + 1}/${contextQuestion.questionnaire.size}"

        buttonNext.text =
            if (contextQuestion.scene == question.context.lastIndex)
                resources.getString(R.string.finish_questionnaire)
            else resources.getString(R.string.next_question)

        buttonNext.setOnClickListener {
            if (answer != "") contextQuestion.nextQuestion()

            if (answer == question.statements[question.truth])
                Toast.makeText(context, "true", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show()
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
                        setOnClickListener {
                            it as RadioButton
                            if (!it.isActivated) answer = text.toString()
                        }
                    })
            Statements.ENTER -> {
                layout.addView(EditText(context).apply {

                })
            }
        }
    }
}
