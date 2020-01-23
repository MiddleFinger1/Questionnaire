package com.questionnaire.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.*
import android.widget.RadioGroup.LayoutParams
import android.support.v7.widget.Toolbar
import com.questionnaire.Question
import com.questionnaire.Statements
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import com.CustomModalWindow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuestionSession : Fragment() {

    // must be initialize
    lateinit var question: Question
    lateinit var contextQuestion: PresentativeQuestionnaire

    private var timer = 0L
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
        views = inflater.inflate(com.R.layout.fragment_question_layout, container, false)
        views.apply {
            toolbar = findViewById(com.R.id.Question_Toolbar)
            questionTitle = findViewById(com.R.id.Question_Question)
            descriptionView = findViewById(com.R.id.Question_Description)
            timerView = findViewById(com.R.id.Question_Timer)
            questionImage = findViewById(com.R.id.Question_Image)
            statementsLayout = findViewById(com.R.id.Question_Statements)
        }
        timerView.text = "0"
        CoroutineScope(Dispatchers.Main).launch {
            sceneTime()
        }
        if (question.icon != null) {
            val icon = openSource(contextQuestion.activity, question.icon!!)
            if (icon is Drawable)
                questionImage.background = icon
        }
        toolbar.title = "${contextQuestion.sceneInstance + 1}/${contextQuestion.questionnaire.maxQuestions}"
        toolbar.inflateMenu(com.R.menu.bottom_nav_questions)
        toolbar.setOnMenuItemClickListener {
                menuItem: MenuItem? ->
            if (menuItem == null)
                return@setOnMenuItemClickListener false
            when (menuItem.itemId){
                com.R.id.MenuQuestion_Exit ->
                    contextQuestion.activity.supportFragmentManager.beginTransaction()
                        .replace(com.R.id.MainQuestionnaireLayout, contextQuestion).commit()
                com.R.id.MenuQuestion_Back ->
                    contextQuestion.backQuestion()
                com.R.id.MenuQuestion_Next -> {
                    if (answer.isNotEmpty()) {
                        question.answer.clear()
                        question.answer.addAll(answer)
                        contextQuestion.obResult.addAnswer(contextQuestion.sceneInstance, question, answer)
                        contextQuestion.nextQuestion()
                    }
                }
            }
            true
        }
        questionTitle.text = question.question
        descriptionView.text = question.description

        createChoice(statementsLayout, question.statements)
        // statements and image soon must be add
        setSaveChoice()
        return views
    }

    private fun sceneTime(){
        if (question.isDefault)
            return
        //contextQuestion.idQuestions.removeAt(contextQuestion.sceneInstance)
        Log.e("ids", contextQuestion.idQuestions.toString())
        timerView.visibility = View.VISIBLE
        contextQuestion
        val modalWindow = CustomModalWindow()
            modalWindow.setTitle = "Задание на время"
            modalWindow.setDescription = "Выполняя задание, вы должны управиться за отведенного время, иначе, увы!"
            modalWindow.action = {
                addButtonAction("Ok"){
                    dismiss()
                    executeTime()
                }
            }
        modalWindow.show(contextQuestion.activity.supportFragmentManager, modalWindow.javaClass.name)
    }

    private fun executeTime(){
        object : CountDownTimer(question.time*1000, 1000L){
            override fun onFinish() {
                Toast.makeText(contextQuestion.activity.baseContext, "Time is over", Toast.LENGTH_SHORT).show()
                question.answer.clear()
                question.answer.addAll(answer)
                contextQuestion.obResult.addAnswer(contextQuestion.sceneInstance, question, answer)
                contextQuestion.nextQuestion()
            }
            override fun onTick(p0: Long) {
                if (!isVisible) cancel()

                timer += 1
                timerView.text = (question.time - timer).toString()
                Log.e("timer", timer.toString())
            }
        }.start()
    }

    private fun setSaveChoice(){
        if (contextQuestion.sceneInstance > contextQuestion.obResult.lastIndex)
            return

        Log.e("ex", contextQuestion.sceneInstance.toString())
        val obItem = contextQuestion.obResult[contextQuestion.sceneInstance]
        Log.e("ex", obItem.toJsonObject())

        answer = obItem.array

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

    private fun createChoice(layout: LinearLayout, statements: Statements){
        radioGroup = RadioGroup(context)
        for (item in statements){
            val view: View
            when (statements.type){
                Statements.SINGLE -> {
                    view = RadioButton(ContextThemeWrapper(context, com.R.style.ItemThemeTextView))
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
                    view = CheckBox(ContextThemeWrapper(context, com.R.style.ItemThemeTextView))
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
                    view = EditText(ContextThemeWrapper(context, com.R.style.ItemThemeTextView))
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
                else -> view = View(ContextThemeWrapper(context, com.R.style.ItemThemeTextView))
            }
            view.apply {
                setBackgroundResource(com.R.drawable.item_style)
                elevation = 5f
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    val dp10 = resources.getDimension(com.R.dimen.dp10).toInt()
                    val dp5 = resources.getDimension(com.R.dimen.dp5).toInt()
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