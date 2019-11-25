package com.rakiwow.gameofsudoku.views

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
        println(parentFragment)
        return inflater.inflate(R.layout.number_picker_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        motionLayout.transitionToEnd()

        val listener = parentFragment as? OnNumberSelectListener

        noNumber.setOnClickListener { listener?.onNumberSelect(0) }
        numberOne.setOnClickListener { listener?.onNumberSelect(1) }
        numberTwo.setOnClickListener { listener?.onNumberSelect(2) }
        numberThree.setOnClickListener { listener?.onNumberSelect(3) }
        numberFour.setOnClickListener { listener?.onNumberSelect(4) }
        numberFive.setOnClickListener { listener?.onNumberSelect(5) }
        numberSix.setOnClickListener { listener?.onNumberSelect(6) }
        numberSeven.setOnClickListener { listener?.onNumberSelect(7) }
        numberEight.setOnClickListener { listener?.onNumberSelect(8) }
        numberNine.setOnClickListener { listener?.onNumberSelect(9) }
        numberPickerBackground.setOnClickListener { listener?.onNumberSelect(10) }
    }

    interface OnNumberSelectListener{
        fun onNumberSelect(number: Int)
    }
}