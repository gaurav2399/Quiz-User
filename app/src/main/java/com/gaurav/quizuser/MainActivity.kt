package com.gaurav.quizuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(mainToolbar as Toolbar?)
        supportActionBar?.title = "My Quiz App"

        takeQuiz.setOnClickListener {
            val intent = Intent(this,TakeExam::class.java)
            startActivity(intent)
        }

        leaderboard.setOnClickListener {
            val intent = Intent(this,Leaderboard::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.logout -> {
                val intent = Intent(this,LoginActivity::class.java)
                ExamSource.source = 1
                startActivity(intent)
                true
            }
            else -> {
                makeToast(msg = "Wrong Choice")
                true
            }
        }

    }
}
