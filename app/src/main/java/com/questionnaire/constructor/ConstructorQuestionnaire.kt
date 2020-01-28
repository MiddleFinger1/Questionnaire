package com.questionnaire.constructor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout.*
import android.widget.Toast
import com.CustomModalWindow
import com.MainActivity
import com.R
import com.databinding.ConstructorQuestionnaireBinding
import com.helper.createItemTextView
import com.helper.setOnTextChange
import com.questionnaire.Question
import com.questionnaire.Questionnaire
import com.questionnaire.Statements


class ConstructorQuestionnaire : Fragment() {

    lateinit var activity: ConstructorActivity
    lateinit var questionnaire: Questionnaire
    private var isConstructed = false
    private lateinit var binding: ConstructorQuestionnaireBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = ConstructorQuestionnaireBinding.inflate(inflater)
        binding.questionnaire = questionnaire
        binding.apply {
            val questionnaire = this@ConstructorQuestionnaire.questionnaire
            for (id in 0..questionnaire.lastIndex)
                createQuestionView(questionnaire[id])

            ConstructorName.setOnTextChange {
                questionnaire.settings.title = this.text.toString()
            }
            ConstructorGroup.setOnTextChange{
                questionnaire.settings.group = this.text.toString()
            }
            ConstructorDescription.setOnTextChange {
                questionnaire.description = this.text.toString()
            }
            ConstructorIsShownAnswers.setOnClickListener {
                if (it is CheckBox)
                    questionnaire.settings.isPresented = it.isChecked
            }
            ConstructorIsAnonymous.setOnClickListener {
                if (it is CheckBox)
                    questionnaire.settings.isAnonymous = it.isChecked
            }
            ConstructorIsRandom.setOnClickListener {
                if (it is CheckBox)
                    questionnaire.isRandom = it.isChecked
            }
            ConstructorIsPrivate.setOnClickListener {
                if (it is CheckBox)
                    questionnaire.settings.isPrivate = it.isChecked
            }
            ConstructorAddQuestion.setOnClickListener {
                Log.e("adding question", "")
                val question = Question("Новый вопрос №${binding.ConstructorLayoutQuestions.childCount}", Statements())
                questionnaire.add(question)
                createQuestionView(question)
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

    private fun createQuestionView(question: Question){
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