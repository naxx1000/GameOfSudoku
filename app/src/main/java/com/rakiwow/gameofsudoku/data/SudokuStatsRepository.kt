package com.rakiwow.gameofsudoku.data

import androidx.lifecycle.LiveData

//The Dao is passed into the repository. It contains all the read/write methods for the database.
class SudokuStatsRepository (private val sudokuStatsDao: SudokuStatsDao){
    //Room will query the live data on a separate thread, and notify the observer when data has changed.
    val allData: LiveData<List<SudokuStats>> = sudokuStatsDao.getAll()
    lateinit var difficultyFilterData: LiveData<List<SudokuStats>>

    //Suspend modifiers are used to tell the compiler to call from a coroutine or another suspending function
    suspend fun getByDifficulty(difficulty: Int){
        sudokuStatsDao.getByDifficulty(difficulty)
    }

    suspend fun deleteAll(){
        sudokuStatsDao.deleteAll()
    }

    suspend fun insert(sudokuStats: SudokuStats){
        sudokuStatsDao.insert(sudokuStats)
    }

    suspend fun delete(sudokuStats: SudokuStats){
        sudokuStatsDao.delete(sudokuStats)
    }

    suspend fun getEachRecord(){

    }
}