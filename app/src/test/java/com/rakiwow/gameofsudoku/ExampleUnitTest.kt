package com.rakiwow.gameofsudoku

import com.rakiwow.gameofsudoku.utils.MySudoku
import org.junit.Test

class ExampleUnitTest {

    val sudoku = MySudoku()
    var grid: Array<IntArray> = Array(9) { IntArray(9) }

    // This test creates a sudoku grid and prints it
    @Test
    fun gridHasOneSolution() {
        grid = sudoku.createGame(11)
        sudoku.printGame(grid)
    }
}
