package com.ui.start

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.Helper
import com.R
import com.google.android.material.textfield.TextInputEditText
import com.json.questionnaire.Source
import com.json.user.Settings
import com.logic.Firing
import java.io.File


class SignUpFragment : Fragment() {

    private lateinit var toolbar: Toolbar
    private lateinit var emailTextView: TextInputEditText
    private lateinit var passwordTextView: TextInputEditText
    private lateinit var loginTextView: TextInputEditText
    private lateinit var buttonCreateUser: Button
    private lateinit var logouser: ImageView

    private var pathToIcon = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val views = inflater.inflate(R.layout.fragment_sign_up, container, false)
        views.apply {
            toolbar = findViewById(R.id.Start_SignUpToolbar)
            emailTextView = findViewById(R.id.Start_SignUpEmail)
            passwordTextView = findViewById(R.id.Start_SignUpPassword)
            loginTextView = findViewById(R.id.Start_SignUpLogin)
            buttonCreateUser = findViewById(R.id.Start_CreateUser)
            logouser = findViewById(R.id.Start_LogoUser)
        }
        toolbar.setOnClickListener {
            val fragment = AuthFragment()
            if (activity != null)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.MainStartActivity, fragment).commit()
        }
        logouser.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/image"
            startActivityForResult(intent, 0)
        }
        buttonCreateUser.setOnClickListener {
            val email = emailTextView.text.toString()
            val password = passwordTextView.text.toString()
            val login = loginTextView.text.toString()

            Firing.signUpUser(email, password){
                if (it.isSuccessful) {
                    Toast.makeText(context, "Аутентификация прошла успешна!", Toast.LENGTH_SHORT).show()
                    var icon = ""
					var path = ""
                    if (pathToIcon.isNotEmpty())
                        icon = Firing.uploadFile(File(pathToIcon), Firing.imagesFolder, "$login.png")
					val file = Helper.stream2file(requireContext().assets.open("user.json"))
                    if (file != null) 
                        path = Firing.uploadFile(file, Firing.usersFolder, "$login.json")
                    val settings = Settings(Source(icon), login)
                    settings.path = path
                    settings.icon.isInSd = false
                    Firing.createUserSettings(settings)
                }
                else Toast.makeText(context, "Аутентификация провалена!", Toast.LENGTH_SHORT).show()
            }
        }
        return views
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            try {
                pathToIcon = Helper.getRealPathFromURI(requireContext(), data.data!!)
                Log.e("pathToImage", pathToIcon)

                logouser.setImageURI(Uri.parse(pathToIcon))
            }
            catch (ex: Exception){
                Log.e("getResultSignUpImage", ex.toString())
            }
        }



    }

}
