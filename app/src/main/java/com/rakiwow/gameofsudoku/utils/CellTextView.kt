package com.rakiwow.gameofsudoku.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import java.lang.StringBuilder

class CellTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    val TAG = "CellTextView"
    val markList = ArrayList<Int>()
    val paint = Paint()

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

    fun removeAllMarks(){
        if (!markList.isEmpty()){
            markList.removeAll(mutableListOf(1,2,3,4,5,6,7,8,9))
        }else{
            Log.d(TAG, "No marks to remove")
        }
    }

    fun hasMark(number: Int): Boolean{
        return markList.contains(number)
    }

    override fun onDraw(canvas: Canvas?) {
        if(!markList.isEmpty()){
            paint.color = Color.BLACK
            paint.textSize = 28f
            paint.textAlign = Paint.Align.CENTER

            for (i in 0 until markList.size){
                val markPositions = getMarkPosition(markList[i])
                paint.textAlign = Paint.Align.CENTER
                canvas?.drawText(markList[i].toString(), markPositions[0], markPositions[1], paint)
            }
        }
        super.onDraw(canvas)
    }

    fun getMarkPosition(markNumber: Int): FloatArray{
        val x: Float
        val y: Float
        val p = width/100*0.90f
        val pos1 = p + ((width) * 0.25).toFloat()
        val pos2 = p + ((width) * 0.50).toFloat()
        val pos3 = p + ((width) * 0.75).toFloat()
        when(markNumber){
            1 -> {
                x = pos1
                y = pos1
            }
            2 -> {
                x = pos2
                y = pos1
            }
            3 -> {
                x = pos3
                y = pos1
            }
            4 -> {
                x = pos1
                y = pos2
            }
            5 -> {
                x = pos2
                y = pos2
            }
            6 -> {
                x = pos3
                y = pos2
            }
            7 -> {
                x = pos1
                y = pos3
            }
            8 -> {
                x = pos2
                y = pos3
            }
            9 -> {
                x = pos3
                y = pos3
            }
            else -> {
                x = 0f
                y = 0f
            }
        }
        return floatArrayOf(x, y)
    }

    fun addSetOfMarks(markSet: String){
        val marks = markSet.split(",")
        for(mark in marks){
            if(!markList.contains(mark.toInt())){
                markList.add(mark.toInt())
            }
        }
    }

    fun getSetOfMarks() : String{
        val sb = StringBuilder()
        for (mark in markList){
            sb.append(mark.toString()).append(",")
        }
        if(sb.isNotEmpty()){
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }
}