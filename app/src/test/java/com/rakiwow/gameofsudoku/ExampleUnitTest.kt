package com.rakiwow.gameofsudoku

import com.rakiwow.gameofsudoku.utils.MySudoku
import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {

    val sudoku = MySudoku()
    var grid: Array<IntArray> = Array(9) { IntArray(9) }

    @Test
    fun gridHasOneSolution() {
        grid = sudoku.createGame(6)
        for (i in 0 until 9){
            for (j in 0 until 9){
                if(grid[i][j] == 0){
                    assertEquals(true, sudoku.checkCellSolutions(i,j))
                }
            }
        }
    }
}
