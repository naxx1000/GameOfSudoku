package com.rakiwow.gameofsudoku.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class CellTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    val markList = ArrayList<Int>()

    fun addMark(number: Int){
        if(!markList.contains(number)){
            markList.add(number)
        }
    }

    fun removeMark(number: Int){
        if(markList.contains(number)){
            markList.remove(number)
        }
    }

    fun drawMarks(){

    }
}