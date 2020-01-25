package com.questionnaire.constructor


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.R
import com.questionnaire.Questionnaire


class ConstructorQuestionnaire : Fragment() {

    private lateinit var questionnaire: Questionnaire

    public val onTextChange = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_constructor_questionnaire, container, false)
    }


}
