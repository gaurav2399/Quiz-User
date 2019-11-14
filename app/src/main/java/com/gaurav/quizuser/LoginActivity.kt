package com.gaurav.quizuser

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.emailText
import kotlinx.android.synthetic.main.activity_login.login
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.signUp
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences:SharedPreferences
    var online:Boolean=false
    var db = FirebaseFirestore.getInstance()
    lateinit var app:FirebaseApp
    override fun onCreate(savedInstanceState: Bundle?) {
        app = try {
            Log.e("no need of initilaisng","yes")
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            // Firebase not initialized automatically, do it manually
            Log.e("firebasenotinitialising","do manually")
            FirebaseApp.initializeApp(this)!!
        }

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this)
        val editor=sharedPreferences.edit()


        if(ExamSource.source == 1) {
            editor.putBoolean("online", false)
            editor.apply()
        }

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this)
        online=sharedPreferences.getBoolean("online",false)

        val studentsCollection=db.collection("Students")

        super.onCreate(savedInstanceState)


        try {
            db = FirebaseFirestore.getInstance(app)
            Log.e("chl ja bro","please")
        }catch (e:NullPointerException){
            Log.e("teri bhn ka","----")
            FirebaseFirestore.getInstance(app)
        }
        if(!online) {
            setContentView(R.layout.activity_login)
            setSupportActionBar(loginToolbar as Toolbar?)
            supportActionBar?.title = "Login"
        }
        else{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        login?.setOnClickListener {
            val email = emailText.text.toString()
            val password = password.text.toString()
            if(email == "" || password == ""){
                makeToast(msg = "All fields must be filled.")
                return@setOnClickListener
            }
            studentsCollection
                .whereEqualTo("email",email).whereEqualTo("password",password).get()
                .addOnSuccessListener { documentSnapshots ->
                    if(documentSnapshots.isEmpty)
                        makeToast(msg = "Your email or password is incorrect")
                    else {
                        makeToast(msg = "Logged in.", duration = Toast.LENGTH_LONG)
                        editor.putBoolean("online",true)
                        editor.apply()
                        val intent= Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener{exception ->
                    Log.e("exception",exception.toString())
                    makeToast(msg = exception.message!!,duration =  Toast.LENGTH_LONG)
                }

        }
        signUp?.setOnClickListener {
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}
