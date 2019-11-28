package com.rakiwow.gameofsudoku.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rakiwow.gameofsudoku.R
import kotlinx.android.synthetic.main.number_picker_layout.*

class NumberPickerFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.number_picker_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motionLayout.transitionToEnd()

        val isMark = arguments?.getBoolean("isMark", false)
        val markList = arguments?.getIntegerArrayList("markList")
        val listener = parentFragment as? OnNumberSelectListener

        if(isMark == null || isMark){
            colorMarkedNumbers(markList)
        }
        noNumber.setOnClickListener { listener?.onNumberSelect(0, isMark) }
        numberOne.setOnClickListener { listener?.onNumberSelect(1, isMark) }
        numberTwo.setOnClickListener { listener?.onNumberSelect(2, isMark) }
        numberThree.setOnClickListener { listener?.onNumberSelect(3, isMark) }
        numberFour.setOnClickListener { listener?.onNumberSelect(4, isMark) }
        numberFive.setOnClickListener { listener?.onNumberSelect(5, isMark) }
        numberSix.setOnClickListener { listener?.onNumberSelect(6, isMark) }
        numberSeven.setOnClickListener { listener?.onNumberSelect(7, isMark) }
        numberEight.setOnClickListener { listener?.onNumberSelect(8, isMark) }
        numberNine.setOnClickListener { listener?.onNumberSelect(9, isMark) }
        numberPickerBackground.setOnClickListener { listener?.onNumberSelect(10, isMark) }
    }

    interface OnNumberSelectListener{
        fun onNumberSelect(number: Int, isMark: Boolean?)
    }

    fun colorMarkedNumbers(markList: ArrayList<Int>?){

        numberOne.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberTwo.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberThree.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberFour.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberFive.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberSix.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberSeven.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberEight.setBackground(resources.getDrawable(R.drawable.number_choices))
        numberNine.setBackground(resources.getDrawable(R.drawable.number_choices))

        for (i in 0 until markList!!.size){
            when(markList[i]){
                1 -> numberOne.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                2 -> numberTwo.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                3 -> numberThree.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                4 -> numberFour.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                5 -> numberFive.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                6 -> numberSix.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                7 -> numberSeven.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                8 -> numberEight.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
                9 -> numberNine.setBackground(resources.getDrawable(R.drawable.number_choices_marked))
            }
        }
    }
}