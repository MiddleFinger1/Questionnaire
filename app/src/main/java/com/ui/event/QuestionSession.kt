package com.ui.event

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.*
import android.widget.RadioGroup.LayoutParams
import androidx.appcompat.widget.Toolbar
import com.json.questionnaire.Question
import com.json.questionnaire.Statements
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import com.ui.custom.CustomModalWindow
import com.R
import com.openSource


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var contextQuestion: PresentativeQuestionnaire
    lateinit var question: Question

    private var answer = arrayListOf<Int>()

    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var questionTitle: TextView
    private lateinit var descriptionView: TextView
    private lateinit var timerView: TextView
    private lateinit var questionImage: ImageView
    private lateinit var statementsLayout: LinearLayout
    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        views = inflater.inflate(R.layout.fragment_question_layout, container, false)
        views.apply {
            toolbar = findViewById(R.id.Question_Toolbar)
            questionTitle = findViewById(R.id.Question_Question)
            descriptionView = findViewById(R.id.Question_Description)
            timerView = findViewById(R.id.Question_Timer)
            questionImage = findViewById(R.id.Question_Image)
            statementsLayout = findViewById(R.id.Question_Statements)
        }
        timerView.text = "0"

        question = contextQuestion.xEngine.question

        if (question.icon != null) {
            val icon = openSource(contextQuestion.activity, question.icon!!)
            if (icon is Drawable)
                questionImage.background = icon
        }
        toolbar.title = "${contextQuestion.xEngine.sceneInstance + 1}/${contextQuestion.questionnaire.maxQuestions}"
        toolbar.inflateMenu(R.menu.bottom_nav_questions)
        toolbar.setOnMenuItemClickListener {
                menuItem: MenuItem? ->
            if (menuItem == null)
                return@setOnMenuItemClickListener false
            when (menuItem.itemId){
                R.id.MenuQuestion_Exit ->
                    contextQuestion.activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.MainQuestionnaireLayout, contextQuestion).commit()
                R.id.MenuQuestion_Back ->{
                    contextQuestion.xEngine.isExit = true
                    contextQuestion.backQuestion()
                }
                R.id.MenuQuestion_Next -> {
                    if (answer.isNotEmpty()) {
                        contextQuestion.xEngine.isExit = true
                        contextQuestion.xEngine.getAnswer(answer)
                        contextQuestion.nextQuestion()
                    }
                }
            }
            true
        }
        questionTitle.text = question.question
        descriptionView.text = question.description

        contextQuestion.xEngine.setOnActionTimeTick {
            timerView.text = (question.time - contextQuestion.xEngine.timer).toString()
            Log.e("timer", contextQuestion.xEngine.timer.toString())
        }

        contextQuestion.xEngine.setOnActionTimeFinish {
            Toast.makeText(contextQuestion.activity.baseContext, "Time is over", Toast.LENGTH_SHORT).show()
            contextQuestion.xEngine.getAnswer(answer)
            contextQuestion.nextQuestion()
        }

        createChoice(statementsLayout, question.statements)
        // statements and image soon must be add
        setSaveChoice()
        beginScene()
        return views
    }

    private fun beginScene(){
        if (question.isDefault) {
            contextQuestion.xEngine.startQuestion()
            return
        }
        timerView.visibility = View.VISIBLE
        contextQuestion
        val modalWindow = CustomModalWindow()
            modalWindow.setTitle = "Задание на время"
            modalWindow.setDescription = "Выполняя задание, вы должны управиться за отведенного время, иначе, увы!"
            modalWindow.action = {
                addButtonAction("Ok", CustomModalWindow.BUTTON_OK){
                    dismiss()
                    contextQuestion.xEngine.startQuestion()
                }
            }
        modalWindow.show(contextQuestion.activity.supportFragmentManager, modalWindow.javaClass.name)
    }

    private fun setSaveChoice(){
        val itemResult = contextQuestion.xEngine.getObItem()
        if (itemResult != null) {
            answer = itemResult.array
            when (question.statements.type){
                Statements.SINGLE -> {
                    val view = radioGroup.getChildAt(answer[0])
                    view as RadioButton
                    view.isChecked = true
                }
                Statements.MULTI -> {
                    for (id in answer){
                        val view = statementsLayout.getChildAt(id)
                        view as CheckBox
                        view.isChecked = true
                    }
                }
                Statements.ENTER -> {
                    val view = statementsLayout.getChildAt(0) as EditText
                    view.text.append(question.statements.entered)
                }
            }
        }
    }

    private fun createChoice(layout: LinearLayout, statements: Statements){
        radioGroup = RadioGroup(context)
        for (item in statements){
            val view: View
            when (statements.type){
                Statements.SINGLE -> {
                    view = RadioButton(ContextThemeWrapper(context, R.style.ItemThemeTextView))
                    view.text = item
                    view.setTextColor(Color.BLACK)
                    view.highlightColor = Color.BLACK
                    view.buttonTintList = ColorStateList(
                        arrayOf(
                            intArrayOf(-android.R.attr.state_enabled),
                            intArrayOf(android.R.attr.state_enabled)
                        ),
                        intArrayOf(Color.BLACK, Color.parseColor("#008577"))
                    )
                    view.setOnClickListener {
                        try {
                            it as RadioButton
                            if (!it.isActivated) {
                                if (answer.isEmpty())
                                    answer.add(radioGroup.indexOfChild(it))
                                else
                                    answer[0] = radioGroup.indexOfChild(it)
                            }
                        }
                        catch (ex: Exception) {
                            Log.e("ex", ex.toString())
                        }
                    }
                    radioGroup.addView(view)
                }
                Statements.MULTI -> {
                    view = CheckBox(ContextThemeWrapper(context, R.style.ItemThemeTextView))
                    view.text = item
                    view.setTextColor(Color.BLACK)
                    view.highlightColor = Color.BLACK
                    view.setOnClickListener {
                        it as CheckBox
                        if (it.isChecked)
                            answer.add(statements.indexOf(it.text.toString()))
                        else
                            answer.removeAt(answer.indexOf(statements.indexOf(it.text.toString())))
                        Log.e("ex", answer.toString())
                    }
                    layout.addView(view)
                }
                Statements.ENTER -> {
                    view = EditText(ContextThemeWrapper(context, R.style.ItemThemeTextView))
                    view.hint = "Enter here"
                    view.setTextColor(Color.BLACK)
                    view.highlightColor = Color.BLACK
                    view.addTextChangedListener(object: TextWatcher{
                        override fun afterTextChanged(p0: Editable?) {}
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            question.statements.entered = view.text.toString()
                            val print =
                                if (question.statements[0] == question.statements.entered)
                                    question.truth[0]
                                else -1
                            if (answer.isEmpty())
                                answer.add(print)
                            else answer[0] = print
                            Log.e("ex", answer.toString())
                        }
                    })
                    layout.addView(view)
                }
                else -> view = View(ContextThemeWrapper(context, R.style.ItemThemeTextView))
            }
            view.apply {
                setBackgroundResource(R.drawable.item_style)
                elevation = 5f
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    val dp10 = resources.getDimension(R.dimen.dp10).toInt()
                    val dp5 = resources.getDimension(R.dimen.dp5).toInt()
                    bottomMargin = dp5
                    marginStart = dp10
                    marginEnd = dp10
                    topMargin = dp5
                }
            }
        }
        if (statements.type == Statements.SINGLE)
            layout.addView(radioGroup)
    }
}