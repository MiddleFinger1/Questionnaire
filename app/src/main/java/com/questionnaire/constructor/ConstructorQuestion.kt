package com.questionnaire.constructor


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.CheckBox
import android.widget.LinearLayout.*
import android.widget.Toast
import com.CustomModalWindow
import com.R
import com.databinding.ConstructorQuestionBinding
import com.helper.createItemTextView
import com.helper.setOnTextChange
import com.questionnaire.Question


class ConstructorQuestion : Fragment() {

    lateinit var question: Question
    lateinit var contextConstructor: ConstructorQuestionnaire
    private lateinit var binding: ConstructorQuestionBinding
    private val array = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = ConstructorQuestionBinding.inflate(inflater)
        binding.question = question
        binding.apply {
            val question = this@ConstructorQuestion.question
            array.addAll(question.statements)
            for (id in 0..question.statements.lastIndex)
                createStatement(question.statements[id])
            ConstQuestionToolbar.setNavigationOnClickListener {
                question.statements.clear()
                question.statements.addAll(array)
                contextConstructor.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.MainConstructor, contextConstructor)
                    .commit()
            }
            ConstQuestionRandomStates.setOnClickListener {
                question.statements.isRandom = (it as CheckBox).isChecked
            }
            ConstQuestionQuestion.setOnTextChange {
                question.question = text.toString()
            }
            ConstQuestionDescription.setOnTextChange {
                question.description = text.toString()
            }
            ConstQuestionIsDefault.setOnTextChange {
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
            ConstQuestionIsBlocked.setOnClickListener {
                question.isBlocked = ConstQuestionIsBlocked.isChecked
            }
            ConstQuestionAddStatement.setOnClickListener {
                val modalWindow = CustomModalWindow()
                modalWindow.setTitle = "Создать ответ"
                modalWindow.setDescription = "Введи текст для создания ответа"
                modalWindow.action = {
                    addTextEdit()
                    addCheckBox("Установить как правильный ответ"){
                        if (isChecked)
                            question.truth.add(array.count())
                        else question.truth.removeAt(question.truth.indexOf(array.count()))
                    }
                    addButtonAction("Отмена", CustomModalWindow.BUTTON_CANCEL){
                        dismiss()
                    }
                    addButtonAction("Создать", CustomModalWindow.BUTTON_OK){
                        if (array.indexOf(entered) < 0) {
                            array.add(entered)
                            dismiss()
                            createStatement(entered)
                        }
                        else Toast.makeText(context, "Есть уже такой ответ", Toast.LENGTH_SHORT).show()
                    }
                }
                modalWindow.show(contextConstructor.activity.supportFragmentManager, modalWindow.javaClass.name)
            }
            ConstQuestionAddStatement.setOnClickListener {
                val modalWindow = CustomModalWindow()
                modalWindow.setTitle = "Создать ответ"
                modalWindow.setDescription = "Введи текст для создания ответа"
                modalWindow.action = {
                    addTextEdit()
                    addCheckBox("Установить как правильный ответ"){
                        if (isChecked)
                            question.truth.add(array.count())
                        else question.truth.removeAt(question.truth.indexOf(array.count()))
                    }
                    addButtonAction("Отмена", CustomModalWindow.BUTTON_CANCEL){
                        dismiss()
                    }
                    addButtonAction("Создать", CustomModalWindow.BUTTON_OK){
                        if (array.indexOf(entered) < 0) {
                            array.add(entered)
                            dismiss()
                            createStatement(entered)
                        }
                        else Toast.makeText(context, "Есть уже такой ответ", Toast.LENGTH_SHORT).show()
                    }
                }
                modalWindow.show(contextConstructor.activity.supportFragmentManager, modalWindow.javaClass.name)
            }
            ConstQuestionMarkCost.setOnTextChange {
                try {
                    val mark = text.toString().toDouble()
                    if (mark != 0.0)
                        question.cost = mark
                }
                catch(ex: Exception){
                    Log.e("setTime", ex.toString())
                }
            }
        }
        return binding.root
    }

    private fun createStatement(string: String){
        val textView = createItemTextView(
            binding.ConstQuestionLayoutStatements.context,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
        )
        textView.text = string
        textView.setOnLongClickListener {
           array.removeAt(array.indexOf(string))

            Log.e("indexStatement", array.indexOf(string).toString())
            binding.ConstQuestionLayoutStatements.removeView(it)
            Log.e("deleteStatement", array.toString())
            true
        }
        textView.setTextColor(
            if (question.truth.indexOf(array.indexOf(string)) >= 0)
                Color.parseColor("#6295C3")
            else Color.BLACK
        )
        textView.setOnClickListener {
            val modalWindow = CustomModalWindow()
            modalWindow.entered = string
            modalWindow.setTitle = "Создать ответ"
            modalWindow.setDescription = "Введи текст для создания ответа"
            modalWindow.action = {
                var index = question.truth.indexOf(array.indexOf(string))
                addTextEdit()
                addCheckBox("Установить как правильный ответ", index >= 0){
                    try {
                        if (index < 0){
                            question.truth.add(array.indexOf(string))
                            index = question.truth[question.truth.lastIndex]
                        }
                        else question.truth.removeAt(index)
                    }
                    catch (ex: Exception) {
                        Log.e("removingFromTruth", ex.toString())
                        Log.e("indexRemoving", index.toString())
                    }
                }
                addButtonAction("Отмена", CustomModalWindow.BUTTON_CANCEL){
                    dismiss()
                }
                addButtonAction("Изменить", CustomModalWindow.BUTTON_CANCEL){
                    array[binding.ConstQuestionLayoutStatements.indexOfChild(it)] = entered
                    textView.text = entered
                    textView.setTextColor(
                        if (question.truth.indexOf(array.indexOf(string)) >= 0)
                            Color.parseColor("#6295C3")
                        else Color.BLACK
                    )
                    dismiss()
                }
            }
            modalWindow.show(contextConstructor.activity.supportFragmentManager, modalWindow.javaClass.name)
        }
        binding.ConstQuestionLayoutStatements.addView(textView)
    }
}
