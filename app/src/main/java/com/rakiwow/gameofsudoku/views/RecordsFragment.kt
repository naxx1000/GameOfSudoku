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
import kotlinx.android.synthetic.main.fragment_records.*
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class RecordsFragment : Fragment() {

    private lateinit var recordsViewModel: RecordsViewModel
    private lateinit var dateFormat: DateFormat

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

        dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, resources.configuration.locale)
        recordsViewModel.recordData.observe(viewLifecycleOwner, Observer{ stats ->
            stats?.let{handleRecords(it)}
        })
    }

    fun handleRecords(records: List<SudokuStats>){
        for (record in records){
            val date: String? = dateFormat.format(record.date?.let { Date(it) })
            val timeMinutes = record.completedTime?.div(60)
            val timeSeconds = record.completedTime?.minus(record.completedTime.div(60) * 60)
            val timeStringBuilder = StringBuilder()
            if(timeMinutes == 1){
                timeStringBuilder.append(timeMinutes.toString() + " Minute ")
            }else if(timeMinutes != 0) {
                timeStringBuilder.append(timeMinutes.toString() + " Minutes ")
            }
            if(timeSeconds == 1){
                timeStringBuilder.append(timeSeconds.toString() + " Second")
            }else if(timeSeconds != 0){
                timeStringBuilder.append(timeSeconds.toString() + " Seconds")
            }
            if(record.difficulty == 0){
                record_difficulty1.text = date + " " + timeStringBuilder
            }else if(record.difficulty == 1){
                record_difficulty2.text = date + " " + timeStringBuilder
            }else if(record.difficulty == 2){
                record_difficulty3.text = date + " " + timeStringBuilder
            }else if(record.difficulty == 3){
                record_difficulty4.text = date + " " + timeStringBuilder
            }else if(record.difficulty == 4){
                record_difficulty5.text = date + " " + timeStringBuilder
            }
        }
    }
}
