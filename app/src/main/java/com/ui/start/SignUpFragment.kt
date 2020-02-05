package com.ui.start

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var emailTextView: TextInputEditText
    private lateinit var passwordTextView: TextInputEditText
    private lateinit var loginTextView: TextInputEditText
    private lateinit var buttonCreateUser: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_sign_up, container, false)
        views.apply {
            toolbar = findViewById(R.id.Start_SignUpToolbar)
            emailTextView = findViewById(R.id.Start_SignUpEmail)
            passwordTextView = findViewById(R.id.Start_SignUpPassword)
            loginTextView = findViewById(R.id.Start_SignUpLogin)
            buttonCreateUser = findViewById(R.id.Start_CreateUser)
        }
        toolbar.setOnClickListener {
            val fragment = AuthFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }
        buttonCreateUser.setOnClickListener {
            val email = emailTextView.text.toString()
            val password = passwordTextView.text.toString()
            try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                        Toast.makeText(context, "Аутентификация прошла успешна!", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "Аутентификация провалена!", Toast.LENGTH_SHORT).show()
                }
            }
            catch (ex: Exception) {
                Toast.makeText(context, "Аутентификация провалена!", Toast.LENGTH_SHORT).show()
                Log.e("signUpException", ex.toString())
            }
        }
        return views
    }

}
