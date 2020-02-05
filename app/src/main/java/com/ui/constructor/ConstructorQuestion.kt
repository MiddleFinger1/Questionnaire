package com.ui.constructor

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.LinearLayout.*
import android.widget.Toast
import com.ui.custom.CustomModalWindow
import com.Helper.getRealPathFromURI
import com.R
import com.databinding.ConstructorQuestionBinding
import com.createItemTextView
import com.setOnTextChange
import com.json.questionnaire.Question
import com.json.questionnaire.Source
import com.json.questionnaire.Statements


class ConstructorQuestion : Fragment() {

    lateinit var question: Question
    lateinit var contextConstructor: ConstructorQuestionnaire
    private lateinit var binding: ConstructorQuestionBinding
    private val array = arrayListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ConstructorQuestionBinding.inflate(inflater)
        binding.question = question
        binding.time = question.time.toString()
        binding.cost = question.cost.toString()
        binding.apply {
            val question = this@ConstructorQuestion.question
            array.addAll(question.statements)
            for (id in 0..question.statements.lastIndex)
                createStatement(question.statements[id])
            ConstQuestionToolbar.setNavigationOnClickListener {
                question.statements.type = when {
                    (question.truth.size == 1) -> Statements.SINGLE
                    (question.truth.size > 1 && question.truth.size < question.statements.size) ->
                        Statements.MULTI
                    (question.truth.size == 0 || question.truth.size == question.statements.size) ->
                        Statements.ENTER
                    else -> -1
                }
                contextConstructor.setConstructed(question)
                question.statements.clear()
                question.statements.addAll(array)
                contextConstructor.activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.MainConstructor, contextConstructor)
                    .commit()
            }
            if (question.icon != null)
                ConstQuestionImage.setImageURI(Uri.parse(question.icon?.path))
            ConstQuestionAddImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/image"
                startActivityForResult(intent, 0)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null){
            try {
                val path = getRealPathFromURI(contextConstructor.activity.baseContext, data.data!!)
                Log.e("pathToImage", path)
                val uri: Uri = Uri.parse(path)
                binding.ConstQuestionImage.setImageURI(uri)
                question.icon = Source(path).apply {
                    isInSd = true
                }
            }
            catch (ex: Exception){
                Log.e("getResultQuestionImage", ex.toString())
            }
        }
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
