package com.rakiwow.gameofsudoku.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.viewmodel.RecordsViewModel

class RecordsFragment : Fragment() {

    private lateinit var recordsViewModel: RecordsViewModel
    private var records = emptyList<SudokuStats>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)

        recordsViewModel.recordData.observe(viewLifecycleOwner, Observer{ stats ->
            stats?.let{records = it}
        })

        println(records.size)
        for (record in records){
            print(record.grid.toString() + " // ")
            print(record.clues.toString() + " // ")
            print(record.date.toString() + " // ")
            print(record.completedTime.toString() + " // ")
            print(record.difficulty.toString() + " \n ")
        }
    }
}
