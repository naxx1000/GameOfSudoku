package com.rakiwow.gameofsudoku.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.rakiwow.gameofsudoku.data.AppDatabase
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.data.SudokuStatsRepository
import kotlinx.coroutines.launch

class StatsViewModel (application: Application) : AndroidViewModel(application){

    private val repository: SudokuStatsRepository
    val statsData: LiveData<List<SudokuStats>>

    init{
        val sudokuStatsDao = AppDatabase.getDatabase(application, viewModelScope).sudokuStatsDao()
        repository = SudokuStatsRepository(sudokuStatsDao)
        statsData = repository.allData
    }

    //We use viewModelScope.launch to call a suspended function
    fun getByDifficulty(difficulty: Int) = viewModelScope.launch {
        repository.getByDifficulty(difficulty)
    }

    fun insert(sudokuStats: SudokuStats) = viewModelScope.launch {
        repository.insert(sudokuStats)
    }

    fun delete(sudokuStats: SudokuStats) = viewModelScope.launch {
        repository.delete(sudokuStats)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}