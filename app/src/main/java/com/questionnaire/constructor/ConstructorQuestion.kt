package com.questionnaire.constructor


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import com.CustomModalWindow
import com.R
import com.helper.createItemTextView
import com.helper.setOnTextChange
import com.questionnaire.Question


class ConstructorQuestion : Fragment() {

    lateinit var question: Question
    lateinit var contextConstructor: ConstructorQuestionnaire
    private var isConstructed = false
    private lateinit var views: View
    private lateinit var toolbar: Toolbar
    private lateinit var questionView: TextInputEditText
    private lateinit var descriptionView: TextInputEditText
    private lateinit var timeTextEdit: TextInputEditText
    private lateinit var checkIsBlocked: CheckBox
    private lateinit var addStatement: Button
    private lateinit var layoutStatements: LinearLayout
    private lateinit var markEditText: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_constructor_question, container, false)
        views.apply {
            toolbar = findViewById(R.id.ConstQuestion_toolbar)
            questionView = findViewById(R.id.ConstQuestion_question)
            descriptionView = findViewById(R.id.ConstQuestion_Description)
            timeTextEdit = findViewById(R.id.ConstQuestion_isDefault)
            checkIsBlocked = findViewById(R.id.ConstQuestion_isBlocked)
            addStatement = findViewById(R.id.ConstQuestion_AddStatement)
            layoutStatements = findViewById(R.id.ConstQuestion_LayoutStatements)
            markEditText = findViewById(R.id.ConstQuestion_MarkCost)
        }
        for (id in 0..question.statements.lastIndex)
            createStatement(question.statements[id])

        questionView.text!!.append(question.question)
        timeTextEdit.text!!.append(question.time.toString())
        checkIsBlocked.isChecked = question.isBlocked
        descriptionView.text!!.append(question.description)

        toolbar.setNavigationOnClickListener {
            contextConstructor.activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.MainConstructor, contextConstructor)
                .commit()
        }
        questionView.setOnTextChange {
            question.question = text.toString()
        }
        descriptionView.setOnTextChange {
            question.description = text.toString()
        }
        timeTextEdit.setOnTextChange {
            try {
                val time = text.toString().toLong()
                if (time in 30..180){
                    question.isDefault = false
                    question.time = time
                }
                else {
                    question.isDefault = true
                }
            }
            catch(ex: Exception){
                Log.e("setTime", ex.toString())
            }
        }
        checkIsBlocked.setOnClickListener {
            question.isBlocked = checkIsBlocked.isChecked
        }
        addStatement.setOnClickListener {
            val modalWindow = CustomModalWindow()
                modalWindow.setTitle = "Создать ответ"
                modalWindow.setDescription = "Введи текст для создания ответа"
                modalWindow.action = {
                    addTextEdit()
                    addCheckBox("Установить как правильный ответ"){
                        if (isChecked)
                            question.truth.add(question.statements.count())
                        else question.truth.removeAt(question.truth.indexOf(question.statements.count()))
                    }
                    addButtonAction("Отмена"){
                        dismiss()
                    }
                    addButtonAction("Создать"){
                        dismiss()
                        createStatement(entered)
                    }
                }
            modalWindow.show(contextConstructor.activity.supportFragmentManager, modalWindow.javaClass.name)
        }
        markEditText.setOnTextChange {
            try {
                val mark = text.toString().toDouble()
                if (mark != 0.0)
                    question.cost = mark
            }
            catch(ex: Exception){
                Log.e("setTime", ex.toString())
            }
        }
        return views
    }

    private fun createStatement(string: String){
        val textView = createItemTextView(
            layoutStatements.context,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
        )
        textView.text = string
        textView.setOnLongClickListener {
            question.statements.removeAt(layoutStatements.indexOfChild(it))
            layoutStatements.removeView(it)
            true
        }
        textView.setOnClickListener {
            val modalWindow = CustomModalWindow()
            modalWindow.entered = string
            modalWindow.setTitle = "Создать ответ"
            modalWindow.setDescription = "Введи текст для создания ответа"
            modalWindow.action = {
                addTextEdit()
                addCheckBox("Установить как правильный ответ"){
                    if (isChecked)
                        question.truth.add(question.statements.count())
                    else question.truth.removeAt(question.truth.indexOf(question.statements.count()))
                }
                addButtonAction("Отмена"){
                    dismiss()
                }
                addButtonAction("Изменить"){
                    dismiss()
                    question.statements[layoutStatements.indexOfChild(it)] = entered
                }
            }
            modalWindow.show(contextConstructor.activity.supportFragmentManager, modalWindow.javaClass.name)
        }
        question.statements.add(string)
        layoutStatements.addView(textView)
    }
}
