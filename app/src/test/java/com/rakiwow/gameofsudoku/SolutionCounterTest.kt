package com.rakiwow.gameofsudoku

import com.rakiwow.gameofsudoku.utils.MySudoku
import org.junit.Test

import org.junit.Assert.*

class SolutionCounterTest {

    val sudoku = MySudoku()
    //Has 4 solutions. Verified on http://www.sudokuwiki.org/sudoku.htm
    var grid2: Array<IntArray> = arrayOf(
        intArrayOf(9, 0, 0, 5, 0, 0, 1, 2, 0),
        intArrayOf(0, 7, 0, 2, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 8, 0, 0, 7, 3, 0, 0),
        intArrayOf(0, 0, 9, 0, 7, 0, 8, 0, 0),
        intArrayOf(0, 8, 0, 0, 0, 0, 0, 5, 0),
        intArrayOf(0, 0, 2, 0, 3, 0, 4, 0, 0),
        intArrayOf(0, 0, 7, 4, 0, 0, 2, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 8, 0, 6, 0),
        intArrayOf(0, 4, 6, 0, 0, 0, 0, 0, 9)
    )
    //Has 2 solutions
    var grid3: Array<IntArray> = arrayOf(
        intArrayOf(9, 0, 0, 5, 0, 0, 1, 2, 0),
        intArrayOf(0, 7, 0, 2, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 8, 0, 0, 7, 3, 0, 0),
        intArrayOf(0, 0, 9, 0, 7, 0, 8, 0, 0),
        intArrayOf(0, 8, 0, 0, 0, 0, 0, 5, 0),
        intArrayOf(0, 0, 2, 0, 3, 0, 4, 0, 0),
        intArrayOf(0, 0, 7, 4, 0, 0, 2, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 8, 0, 6, 0),
        intArrayOf(0, 4, 6, 0, 0, 0, 0, 1, 9)
    )

    @Test
    fun gridHasOneSolution() {
        sudoku.insertGrid(grid3)
        assertEquals(2, sudoku.isThereMoreThanOneSolution())
    }
}
