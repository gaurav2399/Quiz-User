package com.gaurav.quizuser

import android.util.Log
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.exam_item_layout.*


class ExamItem(private val examModal: ExamModal): Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        Log.e("kyu hua binnd","hi")
        viewHolder.apply {
            examTitle.text = examModal.title
            time.text = examModal.time
            questionValue.text = examModal.totalQuestios
        }
    }

    override fun getLayout(): Int  = R.layout.exam_item_layout
}