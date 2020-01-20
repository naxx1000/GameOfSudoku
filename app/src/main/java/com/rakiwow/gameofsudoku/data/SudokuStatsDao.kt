package com.rakiwow.gameofsudoku.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SudokuStatsDao {
    @Query("SELECT * FROM stats ORDER BY date DESC")
    fun getAll(): LiveData<List<SudokuStats>>

    @Query("SELECT * FROM stats WHERE difficulty LIKE :difficulty ORDER BY date DESC")
    fun getByDifficulty(difficulty: Int): LiveData<List<SudokuStats>>

    @Query("SELECT DISTINCT 'difficulty', 'date', 'completedTime' FROM stats WHERE ")
    suspend fun getEachRecord()

    @Query("DELETE FROM stats")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(sudokuStats: SudokuStats)

    @Delete
    suspend fun delete(sudokuStats: SudokuStats)
}