package com.gaurav.quizuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_take_exam.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TakeExam : AppCompatActivity() {

    var db = FirebaseFirestore.getInstance()
    val examOutList: MutableList<ExamModal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_exam)

        setSupportActionBar(takeExamToolbar as Toolbar?)
        supportActionBar?.title = "Take Exam"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val  groupAdapter = GroupAdapter<ViewHolder>()

        runBlocking {
            db.collection("Question Makers").get().addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("Question Makers").document(document.id).collection("Exams").get()
                        .addOnSuccessListener { examsList ->
                            for (exam in examsList) {
                                val item = ExamModal(
                                    exam.get("totalQuestion").toString(),
                                    exam.get("timerValue").toString(), exam.get("title").toString()
                                )
                                groupAdapter.add(ExamItem(item))
                                loading.visibility = View.GONE
                            }
                        }.addOnFailureListener { e ->
                            makeToast(context = this@TakeExam, msg = e.message!!)
                        }
                }
            }.addOnFailureListener { e ->
                makeToast(context = this@TakeExam, msg = e.message!!)
            }
        }
        examsList.layoutManager = LinearLayoutManager(this)
        examsList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        examsList.adapter = groupAdapter

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
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

