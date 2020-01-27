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
        //Paint the background/border
        paint.color = Color.rgb(255,255,255)
        canvas?.drawRect(26f, 18f, 9 * 50f + 23f, 9 * 50f + 15f, paint)
        //Paint each cell
        paint.textSize = 24f
        paint.textAlign = Paint.Align.CENTER
        for (i in 0 until 9){
            for (j in 0 until 9){
                val s = grid?.get(i)?.get(j)
                if(s == 0){
                    paint.color = Color.WHITE
                    val left = 30f + j * 50f
                    val top = 22f + i * 50f
                    canvas?.drawRect(left, top, left + 39f, top + 39f, paint)
                    paint.color = Color.BLACK
                    canvas?.drawText(" ", (1 + j) * 50f, (1 + i) * 50f, paint)
                }else{
                    paint.color = Color.rgb(220,220,220)
                    val left = 30f + j * 50f
                    val top = 22f + i * 50f
                    canvas?.drawRect(left - 5, top - 5, left + 39f + 5, top + 39f + 5, paint)
                    paint.color = Color.BLACK
                    canvas?.drawText(s.toString(), (1 + j) * 50f, (1 + i) * 50f, paint)
                }
            }
        }
        //Paint lines that border the regions
        paint.color = Color.rgb(100,85,80)
        paint.strokeWidth = 5f
        canvas?.drawLine(175f,18f,175f,465f, paint)
        canvas?.drawLine(325f,18f,325f,465f, paint)
        canvas?.drawLine(26f,167f,473f,167f, paint)
        canvas?.drawLine(26f,317f,473f,317f, paint)

        paint.strokeWidth = 1.5f
        //Vertical thin lines
        canvas?.drawLine(74f, 18f, 74f, 465f, paint)
        canvas?.drawLine(124f, 18f, 124f, 465f, paint)
        canvas?.drawLine(224f, 18f, 224f, 465f, paint)
        canvas?.drawLine(274f, 18f, 274f, 465f, paint)
        canvas?.drawLine(374f, 18f, 374f, 465f, paint)
        canvas?.drawLine(424f, 18f, 424f, 465f, paint)
        //Horizontal thin lines
        canvas?.drawLine(26f, 67f,473f, 67f, paint)
        canvas?.drawLine(26f, 117f, 473f, 117f, paint)
        canvas?.drawLine(26f, 217f, 473f, 217f, paint)
        canvas?.drawLine(26f, 267f, 473f, 267f, paint)
        canvas?.drawLine(26f, 367f, 473f, 367f, paint)
        canvas?.drawLine(26f, 417f, 473f, 417f, paint)

        super.onDraw(canvas)
    }
}