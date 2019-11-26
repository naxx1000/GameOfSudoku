package com.rakiwow.gameofsudoku.utils

import java.util.*

class MySudoku {

    var solutionCounter = 0
    val rand: Random = Random()
    var removedCells = 0
    var grid: Array<IntArray> = Array(9) { IntArray(9) }
    var oneDimList: ArrayList<String> = ArrayList(81)
    var oneDimArr = arrayOf(
        "1,1","1,2","1,3","1,4","1,5","1,6","1,7","1,8","1,9",
        "2,1","2,2","2,3","2,4","2,5","2,6","2,7","2,8","2,9",
        "3,1","3,2","3,3","3,4","3,5","3,6","3,7","3,8","3,9",
        "4,1","4,2","4,3","4,4","4,5","4,6","4,7","4,8","4,9",
        "5,1","5,2","5,3","5,4","5,5","5,6","5,7","5,8","5,9",
        "6,1","6,2","6,3","6,4","6,5","6,6","6,7","6,8","6,9",
        "7,1","7,2","7,3","7,4","7,5","7,6","7,7","7,8","7,9",
        "8,1","8,2","8,3","8,4","8,5","8,6","8,7","8,8","8,9",
        "9,1","9,2","9,3","9,4","9,5","9,6","9,7","9,8","9,9"
    )

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
        var maxRemovedCells: Int
        when (difficulty) {
            0 -> maxRemovedCells = 40
            1 -> maxRemovedCells = 42
            2 -> maxRemovedCells = 44
            3 -> maxRemovedCells = 46
            4 -> maxRemovedCells = 48
            5 -> maxRemovedCells = 50
            6 -> maxRemovedCells = 52
            7 -> maxRemovedCells = 53
            8 -> maxRemovedCells = 54
            9 -> maxRemovedCells = 55
            10 -> maxRemovedCells = 56
            404 -> maxRemovedCells = 3 //For debugging
            else -> maxRemovedCells = 58
        }

        loop@ for (n in 0 until 100){
            initGame()
            removedCells = 0

            for (i in 0 until 81){
                oneDimList.add(oneDimArr[i])
            }
            oneDimList.shuffle()
            for (i in 0 until 81){
                if(removeCellNumber(oneDimList[i].split(",")[0].toInt() - 1, oneDimList[i].split(",")[1].toInt() - 1)){
                    if(actualClues() <= 81-maxRemovedCells) break@loop
                }
                if(removeCellNumber(8 - oneDimList[i].split(",")[0].toInt() + 1, 8 - oneDimList[i].split(",")[1].toInt() + 1)){
                    if(actualClues() <= 81-maxRemovedCells) break@loop
                }
            }
        }


        println("Clues: " + actualClues())
        println("Removed cells: " + (81 - actualClues()))
        return grid
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
        oneDimList = ArrayList(81)
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

        value_backup = grid[row][col]
        grid[row][col] = 0
        val solutionCount = isThereMoreThanOneSolution()
        if (solutionCount != 1) {
            grid[row][col] = value_backup
            if(solutionCount == 0) removedCells--
            return false
        }
        removedCells++
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
        if(grid[lastEmptyCell[0]][lastEmptyCell[1]] == 9){
            //Since it iterates through each cell going from 1-9. It must then have
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

    private fun actualClues(): Int{
        var cluesCounter = 0
        for (i in 0 until 9){
            for (j in 0 until 9){
                if(grid[i][j] != 0){
                    cluesCounter++
                }
            }
        }
        return cluesCounter
    }
}
