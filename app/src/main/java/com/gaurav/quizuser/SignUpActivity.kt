package com.gaurav.quizuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    lateinit var studentsCollection:CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(signUpToolbar as Toolbar?)
        val actionBar = supportActionBar
        actionBar?.title = "Sign Up"
        actionBar?.setDisplayHomeAsUpEnabled(true)

        studentsCollection=db.collection("Students")

        login.setOnClickListener {
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val confirm = confirm.text.toString()
            val username = username.text.toString()
            if(email == "" || password == "" || confirm == "" || username == ""){
                makeToast(msg = "Every fields must be filled")
                return@setOnClickListener
            }
            if(password != confirm){
                makeToast(msg = "Both passwords must be same")
                return@setOnClickListener
            }
            GlobalScope.launch {
                if(!emailExist(email)) {
                    Log.e("reach when","yes")
                    val map = mapOf("email" to email, "password" to password, "username" to username)
                    studentsCollection.add(map).addOnSuccessListener {
                        makeToast(msg = "Account created, now you can log in.")
                        finish()
                    }.addOnFailureListener{
                        makeToast(msg = it.message!!)
                    }
                }else{
                    makeToast(this@SignUpActivity,"User with this email already exists!")
                }
            }

        }
    }

    private suspend fun emailExist(email: String):Boolean{
        Log.e("enter in suspend","yes")
        return withContext(Dispatchers.IO) {
            var hasEmail = false
            studentsCollection.whereEqualTo("email", email).get().addOnSuccessListener {
                hasEmail = true
            }
            hasEmail
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                makeToast(msg = "Wrong choice")
                true
            }

        }
    }
}
