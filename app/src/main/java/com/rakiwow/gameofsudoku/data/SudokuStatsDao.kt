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

    //@Query("SELECT date, difficulty, clues, id, grid, MIN(completedTime) FROM stats GROUP BY difficulty")
    @Query("SELECT s.id, s.completedTime, s.date, s.difficulty, s.clues FROM stats s INNER JOIN(SELECT sd.difficulty, MIN(sd.completedTime) AS time FROM stats sd GROUP BY sd.difficulty) sd ON sd.difficulty = s.difficulty and sd.time = s.completedTime")
    fun getEachRecord(): LiveData<List<SudokuStats>>

    @Query("DELETE FROM stats")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(sudokuStats: SudokuStats)

    @Delete
    suspend fun delete(sudokuStats: SudokuStats)
}