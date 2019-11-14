package com.gaurav.quizuser

import android.content.Context
import android.widget.Toast

const val long = Toast.LENGTH_LONG
const val short = Toast.LENGTH_SHORT

fun Context.makeToast(context:Context = this,msg:String, duration:Int = short){
    Toast.makeText(context,msg,duration).show()
}

class ExamSource {
    companion object{
        var source:Int=0
//        fun set(src:Int){
//            source =src
//        }
    }
}