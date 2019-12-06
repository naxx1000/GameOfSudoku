package com.rakiwow.gameofsudoku.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView

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
            paint.textSize = 24f
            paint.textAlign = Paint.Align.CENTER

            for (i in 0 until markList.size){
                val markPositions = getMarkPosition(markList[i])
                canvas?.drawText(markList[i].toString(), markPositions[0], markPositions[1], paint)
            }
        }
        super.onDraw(canvas)
    }

    fun getMarkPosition(markNumber: Int): FloatArray{
        val x: Float
        val y: Float
        when(markNumber){
            1 -> {
                x = 12f
                y = 24f
            }
            2 -> {
                x = 40f
                y = 24f
            }
            3 -> {
                x = 66f
                y = 24f
            }
            4 -> {
                x = 12f
                y = 46f
            }
            5 -> {
                x = 40f
                y = 46f
            }
            6 -> {
                x = 66f
                y = 46f
            }
            7 -> {
                x = 12f
                y = 70f
            }
            8 -> {
                x = 40f
                y = 70f
            }
            9 -> {
                x = 66f
                y = 70f
            }
            else -> {
                x = 0f
                y = 0f
            }
        }
        return floatArrayOf(x, y)
    }
}