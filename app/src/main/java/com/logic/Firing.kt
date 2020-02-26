package com.logic

import android.net.Uri
import android.util.Log
import com.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.json.questionnaire.Source
import com.json.user.Settings
import java.io.File


object Firing {


    val auth = FirebaseAuth.getInstance()

    fun signUpUser(email: String, password: String, action: (Task<AuthResult>) -> Unit){
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                action(it)
            }
        }
        catch (ex: Exception) {
            Log.e("signUpException", ex.toString())
        }
    }

    fun logInUser(email: String, password: String, action: (Task<AuthResult>) -> Unit){
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                action(it)
            }
        }
        catch (ex: Exception){
            Log.e("logInUserException", ex.toString())
        }
    }

    // метод для выгрузки файла в firebase storage
    fun uploadFile(file: File, folder: String): String {
        val reference = FirebaseStorage.getInstance().reference
        val uri = Uri.fromFile(file)
        var path = "$folder/${file.name}"
        if (!file.exists()) return ""
        val refToFile = reference.child(path)
        refToFile.putFile(uri).addOnSuccessListener {
            Log.e("successUpload", it.uploadSessionUri.toString())
        }.addOnFailureListener {
            Log.e("failedUpload", it.message.toString())
            Log.e("failedUpload", it.toString())
            path = ""
        }
        return path
    }

    fun getFile(path: String){

    }

    fun createUserSettings(settings: Settings){
        val user = auth.currentUser ?: return
        val reference = FirebaseDatabase.getInstance().reference
        reference.push().child(user.uid).push().child(SETTINGS)
        val note = reference.child(user.uid).child(SETTINGS)
            note.child(LOGIN).setValue(settings.login)
            note.child(PATH).setValue(settings.path)
            note.child(ICON)
        val icon = note.child(ICON)
            icon.child(PATH).setValue(settings.icon.path)
            icon.child(IS_IN_SD).setValue(settings.icon.isInSd)
            icon.child(TYPE).setValue(settings.icon.type)
    }

    // функция для получения сетингов пользователя из базы данных firebase
    fun onGettingUserSettings(action: (Settings) -> Unit){
        val reference = FirebaseDatabase.getInstance().reference
        val user = auth.currentUser ?: return

        reference.child(user.uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("onCancelledException", p0.toException().toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val array = dataSnapshot.child("Settings")

                val source = Source(array.child("path").value.toString())
                source.isInSd = false
                val settings = Settings(source, array.child("login").value.toString())
                settings.path = array.child("path").value.toString()

                Log.e("Settings", settings.toJsonObject())
                action(settings)
            }
        })
    }
}