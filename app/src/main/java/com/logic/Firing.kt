package com.logic

import com.google.firebase.auth.FirebaseAuth


object Firing {

    val auth = FirebaseAuth.getInstance()

    fun signUpUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

        }
    }

    fun update(){

    }
}