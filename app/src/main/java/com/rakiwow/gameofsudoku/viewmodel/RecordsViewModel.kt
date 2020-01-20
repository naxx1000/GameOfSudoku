package com.rakiwow.gameofsudoku.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rakiwow.gameofsudoku.data.AppDatabase
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.data.SudokuStatsRepository

class RecordsViewModel (application: Application) : AndroidViewModel(application){

    private val repository: SudokuStatsRepository
    val recordData: LiveData<List<SudokuStats>>

    init{
        val sudokuStatsDao = AppDatabase.getDatabase(application, viewModelScope).sudokuStatsDao()
        repository = SudokuStatsRepository(sudokuStatsDao)
        recordData = repository.recordData
    }
}