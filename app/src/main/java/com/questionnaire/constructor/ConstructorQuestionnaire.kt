package com.questionnaire.constructor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.TableRow
import android.widget.Toast
import com.CustomModalWindow
import com.MainActivity
import com.R
import com.databinding.ConstructorQuestionnaireBinding
import com.helper.createEditTextView
import com.helper.createItemTextView
import com.helper.setOnTextChange
import com.questionnaire.Question
import com.questionnaire.Questionnaire
import com.questionnaire.Statements
import kotlinx.android.synthetic.main.constructor_questionnaire.*


class ConstructorQuestionnaire : Fragment() {

    lateinit var activity: ConstructorActivity
    lateinit var questionnaire: Questionnaire
    private var isConstructed = false
    private lateinit var binding: ConstructorQuestionnaireBinding
    private val constructingArray = arrayListOf<Boolean>()
    private var maxMark = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        maxMark = 0.0
        binding = ConstructorQuestionnaireBinding.inflate(inflater)
        binding.questionnaire = questionnaire
        binding.maxQuestions = questionnaire.maxQuestions.toString()
        binding.apply {
            val questionnaire = this@ConstructorQuestionnaire.questionnaire
            for (id in 0..questionnaire.lastIndex) {
                createQuestionView(questionnaire[id])
                setConstructed(id)
                maxMark += questionnaire[id].cost
            }
            for (id in 0..questionnaire.analytics.marks.lastIndex)
                createMarkView(id)
            ConstructorCommonCost.text = "Maximun mark - $maxMark"
            ConstructorAddMark.setOnClickListener {
                questionnaire.analytics.marks.add("")
                questionnaire.analytics.points.add(0.0)
                createMarkView(questionnaire.analytics.points.lastIndex)
            }
            ConstructorMaxQuests.setOnTextChange{
                try {
                    val int = text.toString().toInt()
                    if (int in 0..questionnaire.lastIndex)
                        questionnaire.maxQuestions = int
                }
                catch (ex: Exception){
                    Log.e("exMaxQuests", ex.toString())
                }
            }
            ConstructorAddQuestion.setOnClickListener {
                Log.e("adding question", "")
                val question = Question("Новый вопрос №${ConstructorLayoutQuestions.childCount + 1}", Statements())
                questionnaire.add(question)
                createQuestionView(question)
                maxMark++
                setConstructed(question)
            }
            ConstructorCreate.setOnClickListener {
                if (questionnaire.settings.title != "" && questionnaire.size >= 6){
                    isConstructed = true
                    Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(context, "fill necessary fields!!", Toast.LENGTH_SHORT).show()
            }
            ConstructorFABExit.setOnClickListener {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("questionnaire", questionnaire.toJsonObject())
                intent.putExtra("type", "constructor")
                val modal = CustomModalWindow().apply {
                    setTitle = "Выйти"
                    setDescription = "Вы точно хотите выйти, не закончив анкету?"
                    action = {
                        addButtonAction("Cancel", CustomModalWindow.BUTTON_CANCEL){
                            dismiss()
                        }
                        addButtonAction("I'm sure", CustomModalWindow.BUTTON_OK) {
                            dismiss()
                            this@ConstructorQuestionnaire.activity.startActivity(intent)
                        }
                    }
                }
                modal.show(activity.supportFragmentManager, modal.javaClass.name)
            }
        }
        return binding.root
    }

    private fun setConstructed(id: Int){
        if (id >= 0)
            setConstructed(questionnaire[id], id)
    }

    fun setConstructed(question: Question, id: Int = -1){
        val index =
            if (id >= 0)
                id
            else questionnaire.indexOf(question)
        val bool = question.question != "" && question.statements.size >= 2
        if (questionnaire.size >= constructingArray.size)
            constructingArray.add(bool)
        else constructingArray[index] = bool
    }

    private fun createMarkView(id: Int){
        binding.ConstructorMarkLayout.addView(
            createEditTextView(
                binding.ConstructorMarkLayout.context,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)).apply {
                    text.append(questionnaire.analytics.marks[id])
                    setSingleLine(true)
                    setOnTextChange {
                        questionnaire.analytics.marks[id] = text.toString()
                    }
                    inputType = InputType.TYPE_CLASS_NUMBER
                    setOnLongClickListener {
                        val index = binding.ConstructorMarkLayout.indexOfChild(it)
                        binding.ConstructorCostLayout.removeView(binding.ConstructorCostLayout.getChildAt(index))
                        binding.ConstructorMarkLayout.removeView(it)
                        questionnaire.analytics.marks.removeAt(index)
                        questionnaire.analytics.points.removeAt(index)
                        true
                    }
            }
        )
        binding.ConstructorCostLayout.addView(
            createEditTextView(
                binding.ConstructorCostLayout.context,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)).apply {
                text.append(questionnaire.analytics.points[id].toString())
                setOnTextChange {
                    if (text.toString() != "")
                        questionnaire.analytics.points[id] = text.toString().toDouble()
                }
                setSingleLine(true)
                inputType = InputType.TYPE_CLASS_NUMBER
                setOnLongClickListener {
                    val index = binding.ConstructorCostLayout.indexOfChild(it)
                    binding.ConstructorMarkLayout.removeView(binding.ConstructorMarkLayout.getChildAt(index))
                    binding.ConstructorCostLayout.removeView(it)
                    questionnaire.analytics.marks.removeAt(index)
                    questionnaire.analytics.points.removeAt(index)
                    true
                }
            }
        )
    }

    private fun createQuestionView(question: Question){
        binding.ConstructorCommonCost.text = "Maximun mark - $maxMark"
        val textView = createItemTextView(
            binding.ConstructorLayoutQuestions.context,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
        )
        textView.setTextColor(Color.parseColor("#6295C3"))
        textView.text = question.question
        textView.setOnClickListener {
            Log.e("question", question.toJsonObject())
            val fragment = ConstructorQuestion()
            fragment.question = question
            fragment.contextConstructor = this@ConstructorQuestionnaire
            activity.supportFragmentManager.beginTransaction().replace(R.id.MainConstructor, fragment).commit()
        }
        textView.setOnLongClickListener {
            val modalWindow = CustomModalWindow()
                modalWindow.setTitle = "Удалить вопрос"
                modalWindow.setDescription = "Вы точно хотите удалить вопрос"
                modalWindow.action = {
                    addButtonAction("Ok", CustomModalWindow.BUTTON_OK) {
                        binding.ConstructorCommonCost.text = "Maximun mark - ${maxMark - question.cost}"
                        questionnaire.removeAt(binding.ConstructorLayoutQuestions.indexOfChild(it))
                        binding.ConstructorLayoutQuestions.removeView(it)
                        Log.e("deleteItemFromQuestio", questionnaire.toJsonObject())
                    }
                    addButtonAction("Cancel", CustomModalWindow.BUTTON_CANCEL) {
                        dismiss()
                    }
                }
            modalWindow.show(activity.supportFragmentManager, modalWindow.javaClass.name)
            true
        }
        binding.ConstructorLayoutQuestions.addView(textView)
    }
}