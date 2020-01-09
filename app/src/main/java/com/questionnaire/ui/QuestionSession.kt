package com.questionnaire.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.*
import android.widget.RadioGroup.LayoutParams
import android.support.v7.widget.Toolbar
import com.application.R
import com.questionnaire.Question
import com.questionnaire.Statements
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.*


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var question: Question
    lateinit var contextQuestion: PresentativeQuestionnaire

    private var answer = ""

    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var questionTitle: TextView
    private lateinit var descriptionView: TextView
    private lateinit var questionImage: ImageView
    private lateinit var statementsLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        views = inflater.inflate(R.layout.fragment_question_layout, container, false)
        views.apply {
            toolbar = findViewById(R.id.Question_Toolbar)
            questionTitle = findViewById(R.id.Question_Question)
            descriptionView = findViewById(R.id.Question_Description)
            questionImage = findViewById(R.id.Question_Image)
            statementsLayout = findViewById(R.id.Question_Statements)
        }
        if (question.icon != null) {
            val icon = openSource(contextQuestion.activity, question.icon!!)
            if (icon is Drawable)
                questionImage.background = icon
        }
        toolbar.title = "${contextQuestion.scene + 1}/${contextQuestion.questionnaire.size}"
        toolbar.inflateMenu(R.menu.bottom_nav_questions)
        toolbar.setOnMenuItemClickListener {
                menuItem: MenuItem? ->
            if (menuItem is MenuItem)
                when (menuItem.itemId){
                    R.id.MenuQuestion_Exit ->
                        contextQuestion.activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.MainQuestionnaireLayout, contextQuestion).commit()
                    R.id.MenuQuestion_Back ->
                        contextQuestion.backQuestion()
                    R.id.MenuQuestion_Next -> {
                        if (answer != "") contextQuestion.nextQuestion()
                        if (answer == question.statements[question.truth]) {
                            contextQuestion.points += question.cost
                            Toast.makeText(context, "+${question.cost}", Toast.LENGTH_SHORT).show()
                        }
                        else
                            Toast.makeText(context, "false", Toast.LENGTH_SHORT).show()
                    }
                }
            true
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
                    groupButton.addView(RadioButton(ContextThemeWrapper(context, R.style.ItemThemeTextView)).apply {
                        text = item
                        setTextColor(Color.BLACK)
                        highlightColor = Color.BLACK
                        setBackgroundResource(R.drawable.item_style)
                        elevation = 5f
                        buttonTintList = ColorStateList(
                            arrayOf(
                                intArrayOf(-android.R.attr.state_enabled),
                                intArrayOf(android.R.attr.state_enabled)
                            ),
                            intArrayOf(
                                Color.BLACK,
                                Color.parseColor("#008577")
                            )
                        )
                        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                            val dp10 = resources.getDimension(R.dimen.dp10).toInt()
                            val dp5 = resources.getDimension(R.dimen.dp5).toInt()
                            bottomMargin = dp5
                            marginStart = dp10
                            marginEnd = dp10
                            topMargin = dp5
                        }
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
