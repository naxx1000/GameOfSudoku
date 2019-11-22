package com.rakiwow.gameofsudoku.utils

import java.util.*

class MySudoku {

    val rand : Random = Random()
    val EMPTY_GRID : Array<IntArray> = arrayOf(
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )
    var removedCells = 0
    var grid : Array<IntArray> = Array(9) { IntArray(9) }

    fun generateRow() : List<Int>{
        val row = ArrayList<Int>()
        while (row.size < 9) {
            val randomNum = rand.nextInt(9) + 1
            if (!row.contains(randomNum)) {
                row.add(randomNum)
            }
        }
        return row
    }

    private fun initGame(){
        resetGrid()
        createGameRec()
    }

    private fun createGameRec(): Boolean {

        for (i in 0 until grid.size) {   // Iterate through each column
            for (j in 0 until grid.size) {   // Iterate through each row
                if (grid[i][j] == 0) {    // Check if cell is empty, otherwise iterate to next cell
                    val rn3 = generateRow()
                    for (n in 0 until grid.size) { // Go through numbers 1-9 in the cell
                        if (isSafe(i, j, rn3[n])) { // Check if number is safe in this cell
                            grid[i][j] =
                                rn3[n] // If number is safe in the cell, cell is assigned the number
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
        // Return true all the way up to the root
        return true
    }

    private fun isSafe(i: Int, j: Int, value: Int): Boolean {

        for (k in 0 until grid.size) {
            //Check columns
            if (value == grid[k][j]) {
                return false
            }
            //Check rows
            if (value == grid[i][k]) {
                return false
            }
        }

        //Check regions 1-9
        if (i <= 2 && j <= 2) {                 //Region 1: 1,1
            for (k in 0..2) {
                for (l in 0..2) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i > 2 && i <= 5 && j <= 2) {    //Region 2: 2,1
            for (k in 3..5) {
                for (l in 0..2) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i > 5 && j <= 2) {              //Region 3: 3,1
            for (k in 6..8) {
                for (l in 0..2) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i <= 2 && j <= 5) {             //Region 4: 1,2
            for (k in 0..2) {
                for (l in 3..5) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i > 2 && i <= 5 && j <= 5) {    //Region 5: 2,2
            for (k in 3..5) {
                for (l in 3..5) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i > 5 && j <= 5) {              //Region 6: 3,2
            for (k in 6..8) {
                for (l in 3..5) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i <= 2) {                       //Region 7: 1,3
            for (k in 0..2) {
                for (l in 6..8) {
                    if (grid[k][l] == value) return false
                }
            }
        } else if (i <= 5) {                        //Region 8: 2,3
            for (k in 3..5) {
                for (l in 6..8) {
                    if (grid[k][l] == value) return false
                }
            }
        } else {
            for (k in 6..8) {
                for (l in 6..8) {
                    if (grid[k][l] == value) return false
                }
            }
        }
        return true
    }

    fun resetGrid(){
        grid = EMPTY_GRID
    }

    fun printGame(game: Array<IntArray>) {
        for (i in game.indices) {
            for (j in 0 until game.size - 1) {
                print(game[i][j].toString() + " ")
            }
            print(game[i][game.size - 1])
            println()
        }
    }

    fun createGame(): Array<IntArray>{

        var grid_hardest : Array<IntArray> = EMPTY_GRID
        initGame()

        var maxRemovedCellNumber = 0
        while (maxRemovedCellNumber < 20){ //49
            for(i in 0 until 100){ //1000
                initGame()
                var attempts = 0
                while(attempts < 100){ //1000
                    if(removeCellNumber()){
                        attempts = 0
                    }else{
                        attempts++
                    }
                }
                if(maxRemovedCellNumber < getRemovedCell()){
                    maxRemovedCellNumber = getRemovedCell()
                    grid_hardest = grid
                }
            }
        }
        return grid_hardest
    }

    fun removeCellNumber() : Boolean{
        var value_backup : Int
        var row = rand.nextInt(9)
        var col = rand.nextInt(9)

        while(true){
            if(grid[row][col] == 0){
                row = rand.nextInt(9)
                col = rand.nextInt(9)
            }else{
                value_backup = grid[row][col]
                grid[row][col] = 0
                if(checkCellSolutions(row,col)){
                    removedCells++
                    return true
                }else{
                    grid[row][col] = value_backup
                    return false
                }
            }
        }
    }

    private fun checkCellSolutions(row : Int, col : Int) : Boolean{
        var counter = 0
        for (i in 0 until 9){
            if(isSafe(row,col,i)){
                counter++
            }
        }
        return counter < 2
    }

    fun getRemovedCell() : Int{
        return removedCells
    }

    fun validateBoard(board : Array<IntArray>) : Boolean{
        grid = board
        for (i in 0 until 9){
            for(j in 0 until 9){
                grid[i][j] = 0
                if(!isSafe(i,j,grid[i][j])){
                    return false
                }
            }
        }
        grid = EMPTY_GRID
        return true
    }
}
