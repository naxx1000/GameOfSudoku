package com.rakiwow.gameofsudoku.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rakiwow.gameofsudoku.R

class SudokuCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    val paint: Paint = Paint()
    var grid: Array<IntArray>? = Array(9) { IntArray(9) }

    override fun onDraw(canvas: Canvas?) {
        paint.textSize = 24f
        paint.textAlign = Paint.Align.CENTER
        for (i in 0 until 9){
            for (j in 0 until 9){
                paint.color = Color.WHITE
                val left = 30f + j * 50f
                val top = 22f + i * 50f
                canvas?.drawRect(left, top, left + 39f, top + 39f, paint)
                paint.color = Color.BLACK
                val s = grid?.get(i)?.get(j)
                if(s == 0){
                    canvas?.drawText(" ", (1 + j) * 50f, (1 + i) * 50f, paint)
                }else{
                    canvas?.drawText(s.toString(), (1 + j) * 50f, (1 + i) * 50f, paint)
                }
            }
        }
        paint.color = Color.rgb(145,145,155)
        paint.strokeWidth = 3f
        canvas?.drawLine(175f,18f,175f,465f, paint)
        canvas?.drawLine(325f,18f,325f,465f, paint)
        canvas?.drawLine(26f,167f,473f,167f, paint)
        canvas?.drawLine(26f,317f,473f,317f, paint)

        super.onDraw(canvas)
    }
}