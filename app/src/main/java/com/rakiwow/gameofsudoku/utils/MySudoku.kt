package com.rakiwow.gameofsudoku.utils

import java.util.*

class MySudoku {

    var solutionCounter = 0
    val rand: Random = Random(523432)
    var removedCells = 0
    var grid: Array<IntArray> = Array(9) { IntArray(9) }
    //Will contain the finished version of the current grid, to compare later when the player has finished
    var initGrid: Array<IntArray> = Array(9) { IntArray(9) }
    lateinit var lastEmptyCell: IntArray

    fun generateRow(): List<Int> {
        val row = ArrayList<Int>(9)
        while (row.size < 9) {
            val randomNum = rand.nextInt(9) + 1
            if (!row.contains(randomNum)) {
                row.add(randomNum)
            }
        }
        return row
    }

    fun createGame(difficulty: Int): Array<IntArray> {
        grid = Array(9) { IntArray(9) }
        var maxRemovedCells: Int
        when (difficulty) {
            0 -> maxRemovedCells = 25
            1 -> maxRemovedCells = 28
            2 -> maxRemovedCells = 31
            3 -> maxRemovedCells = 34
            4 -> maxRemovedCells = 38
            5 -> maxRemovedCells = 42
            6 -> maxRemovedCells = 45
            7 -> maxRemovedCells = 48
            8 -> maxRemovedCells = 52
            9 -> maxRemovedCells = 54
            10 -> maxRemovedCells = 58
            else -> maxRemovedCells = 70
        }
        var grid_hardest: Array<IntArray> = Array(9) { IntArray(9) }
        var maxRemovedCellNumber = 0

        initGame()
        removedCells = 0

        //Generate random row
        val rn = generateRow()

        loop@ for (i in 0 until 9) { //Iterate through all 81 cells
            for (j in 0 until 9) {
                //Remove random cells based on the random 9 numbers from 'rn'
                if (removeCellNumber(rn[i] - 1, rn[j] - 1)) {
                    removedCells++
                    // Ensures that not too many cells get removed, based on the difficulty
                    // of the puzzle.
                    if (removedCells >= maxRemovedCells) {
                        break@loop // Breaks the outer loop, appropriately named 'loop'
                    }
                }/*
                if (removeCellNumber(8 - rn[i] + 1, 8 - rn[j] + 1)){
                    removedCells++
                    // Ensures that not too many cells get removed, based on the difficulty
                    // of the puzzle.
                    if (removedCells >= maxRemovedCells) {
                        break@loop // Breaks the outer loop, appropriately named 'loop'
                    }
                }*/
            }

            if (maxRemovedCellNumber <= removedCells) {
                maxRemovedCellNumber = removedCells
                grid_hardest = grid
            }
        }

        println("Clues: " + (81 - maxRemovedCellNumber).toString())
        println("Removed cells: " + maxRemovedCellNumber.toString())
        return grid_hardest
    }

    fun insertGrid(arrArr: Array<IntArray>){
        grid = arrArr
        lastEmptyCell = findLastEmptyCell()
    }

    private fun initGame() {
        resetGrid() //Must reset all cells in the grid to 0 before a new game can be created
        createGameRec() //Recursive function to create a sudoku board
    }

    private fun createGameRec(): Boolean {
        for (i in 0 until grid.size) {   // Iterate through each column
            for (j in 0 until grid.size) {   // Iterate through each row
                if (grid[i][j] == 0) {    // Check if cell is empty, otherwise iterate to next cell
                    val rn = generateRow()
                    for (n in 0 until grid.size) { // Go through numbers 1-9 in the cell
                        if (isSafe(i, j, rn[n])) { // Check if number is safe in this cell
                            grid[i][j] =
                                rn[n] // If number is safe in the cell, cell is assigned the number
                            if (createGameRec()) { // If child is true, return true to root
                                return true
                            } else {              // If child has is false, set cell to zero
                                grid[i][j] = 0
                            }
                        }
                    }
                    // Return false and go to previous cell
                    return false
                }
            }
        }
        // Base case: If every cell in the grid is not 0, return true.
        return true // Return true all the way up to the root.
    }

    private fun isSafe(i: Int, j: Int, value: Int): Boolean {

        if (value == 0) {
            //println("is 0")
            return false
        }

        for (k in 0 until grid.size) {
            //Check columns
            if (value == grid[k][j] && k != i) {
                //println("column")
                return false
            }
            //Check rows
            if (value == grid[i][k] && k != j) {
                //println("row")
                return false
            }
        }

        //Check regions 1-9
        if (i <= 2 && j <= 2) {                 //Region 1: 1,1
            for (k in 0..2) {
                for (l in 0..2) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i > 2 && i <= 5 && j <= 2) {    //Region 2: 2,1
            for (k in 3..5) {
                for (l in 0..2) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i > 5 && j <= 2) {              //Region 3: 3,1
            for (k in 6..8) {
                for (l in 0..2) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i <= 2 && j <= 5) {             //Region 4: 1,2
            for (k in 0..2) {
                for (l in 3..5) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i > 2 && i <= 5 && j <= 5) {    //Region 5: 2,2
            for (k in 3..5) {
                for (l in 3..5) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i > 5 && j <= 5) {              //Region 6: 3,2
            for (k in 6..8) {
                for (l in 3..5) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i <= 2) {                       //Region 7: 1,3
            for (k in 0..2) {
                for (l in 6..8) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else if (i <= 5) {                        //Region 8: 2,3
            for (k in 3..5) {
                for (l in 6..8) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        } else {
            for (k in 6..8) {
                for (l in 6..8) {
                    if (grid[k][l] == value && k != i && l != j) return false
                }
            }
        }
        return true
    }

    fun resetGrid() {
        grid = Array(9) { IntArray(9) }
    }

    fun printGame(game: Array<IntArray>) {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                print(game[i][j].toString() + " ")
            }
            println("\n")
        }
        println("----------------")
    }

    fun removeCellNumber(row: Int, col: Int): Boolean {
        val value_backup: Int

        while (true) {
            value_backup = grid[row][col]
            grid[row][col] = 0
            if (isThereMoreThanOneSolution() != 1) {
                grid[row][col] = value_backup
                println("row: $row, col: $col, solutions: $solutionCounter, value: $value_backup")
                return false
            }
            return true
        }
    }

    //TODO this is wrong. create new recursive solver that solves and counts possible solutions
    fun checkCellSolutions(): Boolean {
        var counter: Int
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (grid[i][j] == 0) {
                    counter = 0
                    for (n in 0 until 9) {
                        if (isSafe(i, j, n + 1)) {
                            counter++
                            if (counter > 2) {
                                return false
                            }
                        }
                    }
                    if(counter == 0){
                        return false
                    }
                }
            }
        }
        return true
    }

    fun isThereMoreThanOneSolution(): Int{
        solutionCounter = 0
        lastEmptyCell = findLastEmptyCell()
        solutionCounter()
        return solutionCounter
    }

    private fun solutionCounter(): Boolean {
        for (i in 0 until grid.size) {   // Iterate through each column
            for (j in 0 until grid.size) {   // Iterate through each row
                if (grid[i][j] == 0) {    // Check if cell is empty, otherwise iterate to next cell
                    val rn = generateRow()
                    for (n in 0 until grid.size) { // Go through numbers 1-9 in the cell
                        if (isSafe(i, j, n+1)) { // Check if number is safe in this cell
                            grid[i][j] =
                                n+1 // If number is safe in the cell, cell is assigned the number
                            if (solutionCounter()) { // If child is true, return true to root

                            } else {              // If child has is false, set cell to zero
                                grid[i][j] = 0
                            }
                        }
                    }
                    // Return false and go to previous cell
                    return false
                }
            }
        }
        // Base case: If every cell in the grid is not 0, return true.
        solutionCounter++
        if(grid[lastEmptyCell[0]][lastEmptyCell[1]] == 9){ //Since it iterates through each cell going from 1-9. It must then have
            // checked all possible permutations of the board when the last cell in the grid is
            // equal to nine
            return true
        }
        //if(solutionCounter > 1) return true
        return false
    }

    private fun findLastEmptyCell(): IntArray{
        for (i in 0 until 9){
            for (j in 0 until 9){
                if(grid[8-i][8-j] == 0){
                    return intArrayOf(8-i,8-j)
                }
            }
        }
        return intArrayOf(8,8)
    }

    fun validateBoard(board: Array<IntArray>): Boolean {
        //TODO compare with previous finished grid instead
        grid = board
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (!isSafe(i, j, grid[i][j])) {
                    return false
                }
            }
        }
        grid = Array(9) { IntArray(9) }
        return true
    }

    fun isGridFilled(board: Array<IntArray>): Boolean {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (board[i][j] == 0) return false
            }
        }
        return true
    }
}
