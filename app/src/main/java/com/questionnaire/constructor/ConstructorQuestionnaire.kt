package com.questionnaire.constructor


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.CustomModalWindow
import com.MainActivity
import com.R
import com.questionnaire.Questionnaire


class ConstructorQuestionnaire : Fragment() {

    lateinit var activity: ConstructorActivity
    lateinit var questionnaire: Questionnaire
    private var isConstructed = false

    private lateinit var views: View
    private lateinit var fabExit: FloatingActionButton
    private lateinit var fabStart: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        views = inflater.inflate(R.layout.fragment_constructor_questionnaire, container, false)
        views.apply {
            fabExit = findViewById(R.id.Constructor_FABExit)
            fabStart = findViewById(R.id.Constructor_FABAddImage)
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
}
