package com.ui.start

import android.content.Intent
import android.os.Bundle
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
import com.logic.Firing
import com.logic.IOManager
import com.ui.MainActivity


class AuthFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var buttonSignUp: Button
    private lateinit var buttonLogIn: Button
    private lateinit var buttonCreateUser: Button
    private lateinit var emailTextView: TextInputEditText
    private lateinit var passwordTextView: TextInputEditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val views = inflater.inflate(R.layout.fragment_auth, container, false)
        views.apply {
            toolbar = findViewById(R.id.Start_EnterAppToolbar)
            buttonSignUp = findViewById(R.id.Start_SignUp)
            buttonLogIn = findViewById(R.id.Start_LogIn)
            buttonCreateUser = findViewById(R.id.Start_CreateUnsignedUser)
            emailTextView = findViewById(R.id.Start_EmailTextView)
            passwordTextView = findViewById(R.id.Start_PasswordTextView)
        }

        buttonSignUp.setOnClickListener {
            val fragment = SignUpFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }
        buttonCreateUser.setOnClickListener {
            IOManager.onGettingRule = {
                IOManager.createUserInstance(this.requireContext())
                startActivity(Intent(context, MainActivity::class.java))
                Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show()
            }
            if (!IOManager.isGottenRulesOfFS(requireContext()))
                IOManager.onGettingRule()
            else IOManager.getRulesOfFS(requireContext())
        }
        buttonLogIn.setOnClickListener {
            val email = emailTextView.text.toString()
            val password = passwordTextView.text.toString()

            Firing.logInUser(email, password) {
                if (it.isSuccessful)
                    Toast.makeText(context, "Авторизация прошла успешна!", Toast.LENGTH_SHORT).show()
                else Toast.makeText(context, "Авторизация провалена!", Toast.LENGTH_SHORT).show()
            }

            val user = FirebaseAuth.getInstance().currentUser
            if (user != null)
                Firing.onGettingUserSettings {
                    IOManager.onGettingRule = {
                        IOManager.createFileDir()
                        IOManager.writeFile(IOManager.dataFileName, it.toJsonObject())
                    }
                    if (!IOManager.isGottenRulesOfFS(requireContext())){
                        IOManager.onGettingRule()
                    }
                    else IOManager.getRulesOfFS(requireContext())


                }

        }
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        return views
    }

}
