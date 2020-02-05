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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AuthFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var buttonSignUp: Button
    private lateinit var buttonLogIn: Button
    private lateinit var emailTextView: TextInputEditText
    private lateinit var passwordTextView: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_auth, container, false)
        views.apply {
            toolbar = findViewById(R.id.Start_EnterAppToolbar)
            buttonSignUp = findViewById(R.id.Start_SignUp)
            buttonLogIn = findViewById(R.id.Start_LogIn)
            emailTextView = findViewById(R.id.Start_EmailTextView)
            passwordTextView = findViewById(R.id.Start_PasswordTextView)
        }
        buttonSignUp.setOnClickListener {
            val fragment = SignUpFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }
        buttonLogIn.setOnClickListener {
            val email = emailTextView.text.toString()
            val password = passwordTextView.text.toString()
            try {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                        Toast.makeText(context, "Аутентификация прошла успешна!", Toast.LENGTH_SHORT).show()
                    else Toast.makeText(context, "Аутентификация провалена!", Toast.LENGTH_SHORT).show()
                }

                val user = FirebaseAuth.getInstance().currentUser
                val reference = FirebaseDatabase.getInstance().reference

                if (user != null) {
                    reference.child(user.uid).addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("onCancelledException", p0.toException().toString())
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val array = dataSnapshot.child("Settings")

                            Toast.makeText(context, array.toString(), Toast.LENGTH_SHORT).show()

                        }
                    })
                }
            }
            catch (ex: Exception){
                Toast.makeText(context, "Аутентификация провалена!", Toast.LENGTH_SHORT).show()
                Log.e("signUpException", ex.toString())
            }
        }
        toolbar.setNavigationOnClickListener {
            activity?.finish()
        }
        return views
    }

}
