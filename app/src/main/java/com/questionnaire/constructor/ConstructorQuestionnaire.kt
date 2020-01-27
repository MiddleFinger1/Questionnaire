package com.questionnaire.constructor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.Toast
import com.CustomModalWindow
import com.MainActivity
import com.R
import com.helper.createItemTextView
import com.helper.setOnTextChange
import com.questionnaire.Question
import com.questionnaire.Questionnaire
import com.questionnaire.Statements


class ConstructorQuestionnaire : Fragment() {

    lateinit var activity: ConstructorActivity
    lateinit var questionnaire: Questionnaire
    private var isConstructed = false

    private lateinit var views: View
    private lateinit var fabExit: FloatingActionButton
    private lateinit var fabStart: FloatingActionButton
    private lateinit var nameTextView: TextInputEditText
    private lateinit var groupTextView: TextInputEditText
    private lateinit var descriptionTextView: TextInputEditText
    private lateinit var checkShownAnswers: CheckBox
    private lateinit var checkIsAnonymous: CheckBox
    private lateinit var checkIsRandom: CheckBox
    private lateinit var checkIsPrivate: CheckBox
    private lateinit var addQuestionButton: Button
    private lateinit var layoutQuestions: LinearLayout
    private lateinit var createButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_constructor_questionnaire, container, false)
        views.apply {
            fabExit = findViewById(R.id.Constructor_FABExit)
            fabStart = findViewById(R.id.Constructor_FABAddImage)
            nameTextView = findViewById(R.id.Constructor_Name)
            groupTextView = findViewById(R.id.Constructor_Group)
            descriptionTextView = findViewById(R.id.Constructor_Description)
            checkShownAnswers = findViewById(R.id.Constructor_isShownAnswers)
            checkIsAnonymous = findViewById(R.id.Constructor_isAnonymous)
            checkIsRandom = findViewById(R.id.Constructor_IsRandom)
            checkIsPrivate = findViewById(R.id.Constructor_IsPrivate)
            addQuestionButton = findViewById(R.id.Constructor_AddQuestion)
            layoutQuestions = findViewById(R.id.Constructor_LayoutQuestions)
            createButton = findViewById(R.id.Constructor_Create)
        }
        for (id in 0..questionnaire.lastIndex)
            createQuestionView(questionnaire[id])
        nameTextView.text!!.append(questionnaire.settings.title)
        groupTextView.text!!.append(questionnaire.settings.group)
        descriptionTextView.text!!.append(questionnaire.description)
        checkIsPrivate.isChecked = questionnaire.settings.isPrivate
        checkIsRandom.isChecked = questionnaire.isRandom
        checkIsAnonymous.isChecked = questionnaire.settings.isAnonymous
        checkShownAnswers.isChecked = questionnaire.settings.isPresented

        nameTextView.setOnTextChange {
            questionnaire.settings.title = this.text.toString()
        }
        groupTextView.setOnTextChange{
            questionnaire.settings.group = this.text.toString()
        }
        descriptionTextView.setOnTextChange {
            questionnaire.description = this.text.toString()
        }
        checkShownAnswers.setOnClickListener {
            if (it is CheckBox)
                questionnaire.settings.isPresented = it.isChecked
        }
        checkIsAnonymous.setOnClickListener {
            if (it is CheckBox)
                questionnaire.settings.isAnonymous = it.isChecked
        }
        checkIsRandom.setOnClickListener {
            if (it is CheckBox)
                questionnaire.isRandom = it.isChecked
        }
        checkIsPrivate.setOnClickListener {
            if (it is CheckBox)
                questionnaire.settings.isPrivate = it.isChecked
        }
        addQuestionButton.setOnClickListener {
            Log.e("adding question", "")
            val question = Question("Новый вопрос №${layoutQuestions.childCount}", Statements())
            questionnaire.add(question)
            createQuestionView(question)
        }
        createButton.setOnClickListener {
            if (questionnaire.settings.title != "" && questionnaire.size >= 6){
                isConstructed = true
                Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(context, "fill necessary fields!!", Toast.LENGTH_SHORT).show()
        }
        fabExit.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("questionnaire", questionnaire.toJsonObject())
                intent.putExtra("type", "constructor")
            val modal = CustomModalWindow().apply {
                setTitle = "Выйти"
                setDescription = "Вы точно хотите выйти, не закончив анкету?"
                action = {
                    addButtonAction("Cancel"){
                        dismiss()
                    }
                    addButtonAction("I'm sure") {
                        dismiss()
                        this@ConstructorQuestionnaire.activity.startActivity(intent)
                    }
                }
            }
            modal.show(activity.supportFragmentManager, modal.javaClass.name)
        }
        return views
    }

    private fun createQuestionView(question: Question){
        val textView = createItemTextView(
            layoutQuestions.context,
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
            questionnaire.removeAt(layoutQuestions.indexOfChild(it))
            layoutQuestions.removeView(it)
            Log.e("deleteItemFromQuestio", questionnaire.toJsonObject())
            true
        }
        layoutQuestions.addView(textView)
    }
}
