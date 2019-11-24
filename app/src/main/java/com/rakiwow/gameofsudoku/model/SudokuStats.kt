package com.rakiwow.gameofsudoku.model

data class SudokuStats(
    val difficulty: Int,
    val date: Long,
    val completedTime: Int,
    val grid: Array<IntArray> //Not going to use equals() or hashcode()
)