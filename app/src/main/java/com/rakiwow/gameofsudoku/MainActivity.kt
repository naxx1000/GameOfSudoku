package com.rakiwow.gameofsudoku

import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.rakiwow.gameofsudoku.utils.MySudoku
import com.rakiwow.gameofsudoku.views.NumberPickerFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    val sudoku : MySudoku = MySudoku()
    val numberFragment = NumberPickerFragment()

    var rowCtx : Int = 0
    var colCtx : Int = 0
    lateinit var cellCtx : TextView
    var game : Array<IntArray> = arrayOf(
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createPuzzle()

        setUpGrid(game)

        //Add on click listeners to the 81 text views
        sudokuOnClickListeners()
    }

    //TODO Lock initial clues of the sudoku puzzle, and also change the color of them
    fun setUpGrid(grid : Array<IntArray>){
        //Row 1
        cell_11.text = checkIfZero(grid[0][0])
        cell_21.text = checkIfZero(grid[0][1])
        cell_31.text = checkIfZero(grid[0][2])
        cell_41.text = checkIfZero(grid[0][3])
        cell_51.text = checkIfZero(grid[0][4])
        cell_61.text = checkIfZero(grid[0][5])
        cell_71.text = checkIfZero(grid[0][6])
        cell_81.text = checkIfZero(grid[0][7])
        cell_91.text = checkIfZero(grid[0][8])

        //Row 2
        cell_12.text = checkIfZero(grid[1][0])
        cell_22.text = checkIfZero(grid[1][1])
        cell_32.text = checkIfZero(grid[1][2])
        cell_42.text = checkIfZero(grid[1][3])
        cell_52.text = checkIfZero(grid[1][4])
        cell_62.text = checkIfZero(grid[1][5])
        cell_72.text = checkIfZero(grid[1][6])
        cell_82.text = checkIfZero(grid[1][7])
        cell_92.text = checkIfZero(grid[1][8])

        //Row 3
        cell_13.text = checkIfZero(grid[2][0])
        cell_23.text = checkIfZero(grid[2][1])
        cell_33.text = checkIfZero(grid[2][2])
        cell_43.text = checkIfZero(grid[2][3])
        cell_53.text = checkIfZero(grid[2][4])
        cell_63.text = checkIfZero(grid[2][5])
        cell_73.text = checkIfZero(grid[2][6])
        cell_83.text = checkIfZero(grid[2][7])
        cell_93.text = checkIfZero(grid[2][8])

        //Row 4
        cell_14.text = checkIfZero(grid[3][0])
        cell_24.text = checkIfZero(grid[3][1])
        cell_34.text = checkIfZero(grid[3][2])
        cell_44.text = checkIfZero(grid[3][3])
        cell_54.text = checkIfZero(grid[3][4])
        cell_64.text = checkIfZero(grid[3][5])
        cell_74.text = checkIfZero(grid[3][6])
        cell_84.text = checkIfZero(grid[3][7])
        cell_94.text = checkIfZero(grid[3][8])

        //Row 5
        cell_15.text = checkIfZero(grid[4][0])
        cell_25.text = checkIfZero(grid[4][1])
        cell_35.text = checkIfZero(grid[4][2])
        cell_45.text = checkIfZero(grid[4][3])
        cell_55.text = checkIfZero(grid[4][4])
        cell_65.text = checkIfZero(grid[4][5])
        cell_75.text = checkIfZero(grid[4][6])
        cell_85.text = checkIfZero(grid[4][7])
        cell_95.text = checkIfZero(grid[4][8])

        //Row 6
        cell_16.text = checkIfZero(grid[5][0])
        cell_26.text = checkIfZero(grid[5][1])
        cell_36.text = checkIfZero(grid[5][2])
        cell_46.text = checkIfZero(grid[5][3])
        cell_56.text = checkIfZero(grid[5][4])
        cell_66.text = checkIfZero(grid[5][5])
        cell_76.text = checkIfZero(grid[5][6])
        cell_86.text = checkIfZero(grid[5][7])
        cell_96.text = checkIfZero(grid[5][8])

        //Row 7
        cell_17.text = checkIfZero(grid[6][0])
        cell_27.text = checkIfZero(grid[6][1])
        cell_37.text = checkIfZero(grid[6][2])
        cell_47.text = checkIfZero(grid[6][3])
        cell_57.text = checkIfZero(grid[6][4])
        cell_67.text = checkIfZero(grid[6][5])
        cell_77.text = checkIfZero(grid[6][6])
        cell_87.text = checkIfZero(grid[6][7])
        cell_97.text = checkIfZero(grid[6][8])

        //Row 8
        cell_18.text = checkIfZero(grid[7][0])
        cell_28.text = checkIfZero(grid[7][1])
        cell_38.text = checkIfZero(grid[7][2])
        cell_48.text = checkIfZero(grid[7][3])
        cell_58.text = checkIfZero(grid[7][4])
        cell_68.text = checkIfZero(grid[7][5])
        cell_78.text = checkIfZero(grid[7][6])
        cell_88.text = checkIfZero(grid[7][7])
        cell_98.text = checkIfZero(grid[7][8])

        //Row 9
        cell_19.text = checkIfZero(grid[8][0])
        cell_29.text = checkIfZero(grid[8][1])
        cell_39.text = checkIfZero(grid[8][2])
        cell_49.text = checkIfZero(grid[8][3])
        cell_59.text = checkIfZero(grid[8][4])
        cell_69.text = checkIfZero(grid[8][5])
        cell_79.text = checkIfZero(grid[8][6])
        cell_89.text = checkIfZero(grid[8][7])
        cell_99.text = checkIfZero(grid[8][8])

    }

    fun newPuzzle(view : View){
        createPuzzle()
    }

    fun createPuzzle(){
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            button_generate.visibility = View.INVISIBLE
            val asyncTask = async(Dispatchers.IO) {
                createPuzzleBackground()
            }
            asyncTask.await()
            progressBar.visibility = View.GONE
            button_generate.visibility = View.VISIBLE
        }
    }

    suspend fun createPuzzleBackground() = coroutineScope {
        launch{
            game = sudoku.createGame()
            setUpGrid(game)
        }
    }

    fun fragmentIngress(){
        supportFragmentManager.beginTransaction().add(R.id.fragmentViewGroup, numberFragment)
            .commit()
        main_constraint_layout.setBackgroundColor(Color.WHITE)
    }

    fun fragmentEgress(){
        supportFragmentManager.beginTransaction().remove(numberFragment).commit()
        cellCtx.background = getDrawable(R.drawable.cell)
        cellCtx.setTextColor(Color.WHITE)
    }

    fun fragmentClose(view : View){
        fragmentEgress()
    }

    fun sudokuOnClickListeners(){
        //TODO "Fragment already added" error when tapping fast on a cell
        //TODO put all fragmentIngress() inside submitCellNumber function
        cell_11.setOnClickListener {
            submitCellNumber(it, 1, 1)
            fragmentIngress() }
        cell_12.setOnClickListener {
            submitCellNumber(it, 1, 2)
            fragmentIngress()}
        cell_13.setOnClickListener {
            submitCellNumber(it, 1, 3)
            fragmentIngress()}
        cell_14.setOnClickListener {
            submitCellNumber(it, 1, 4)
            fragmentIngress()}
        cell_15.setOnClickListener {
            submitCellNumber(it, 1, 5)
            fragmentIngress()}
        cell_16.setOnClickListener {
            submitCellNumber(it, 1, 6)
            fragmentIngress()}
        cell_17.setOnClickListener {
            submitCellNumber(it, 1, 7)
            fragmentIngress()}
        cell_18.setOnClickListener {
            submitCellNumber(it, 1, 8)
            fragmentIngress()}
        cell_19.setOnClickListener {
            submitCellNumber(it, 1, 9)
            fragmentIngress()}
        cell_21.setOnClickListener {
            submitCellNumber(it, 2, 1)
            fragmentIngress()}
        cell_22.setOnClickListener {
            submitCellNumber(it, 2, 2)
            fragmentIngress()}
        cell_23.setOnClickListener {
            submitCellNumber(it, 2, 3)
            fragmentIngress()}
        cell_24.setOnClickListener {
            submitCellNumber(it, 2, 4)
            fragmentIngress()}
        cell_25.setOnClickListener {
            submitCellNumber(it, 2, 5)
            fragmentIngress()}
        cell_26.setOnClickListener {
            submitCellNumber(it, 2, 6)
            fragmentIngress()}
        cell_27.setOnClickListener {
            submitCellNumber(it, 2, 7)
            fragmentIngress()}
        cell_28.setOnClickListener {
            submitCellNumber(it, 2, 8)
            fragmentIngress()}
        cell_29.setOnClickListener {
            submitCellNumber(it, 2, 9)
            fragmentIngress()}
        cell_31.setOnClickListener {
            submitCellNumber(it, 3, 1)
            fragmentIngress()}
        cell_32.setOnClickListener {
            submitCellNumber(it, 3, 2)
            fragmentIngress()}
        cell_33.setOnClickListener {
            submitCellNumber(it, 3, 3)
            fragmentIngress()}
        cell_34.setOnClickListener {
            submitCellNumber(it, 3, 4)
            fragmentIngress()}
        cell_35.setOnClickListener {
            submitCellNumber(it, 3, 5)
            fragmentIngress()}
        cell_36.setOnClickListener {
            submitCellNumber(it, 3, 6)
            fragmentIngress()}
        cell_37.setOnClickListener {
            submitCellNumber(it, 3, 7)
            fragmentIngress()}
        cell_38.setOnClickListener {
            submitCellNumber(it, 3, 8)
            fragmentIngress()}
        cell_39.setOnClickListener {
            submitCellNumber(it, 3, 9)
            fragmentIngress()}
        cell_41.setOnClickListener {
            submitCellNumber(it, 4, 1)
            fragmentIngress()}
        cell_42.setOnClickListener {
            submitCellNumber(it, 4, 2)
            fragmentIngress()}
        cell_43.setOnClickListener {
            submitCellNumber(it, 4, 3)
            fragmentIngress()}
        cell_44.setOnClickListener {
            submitCellNumber(it, 4, 4)
            fragmentIngress()}
        cell_45.setOnClickListener {
            submitCellNumber(it, 4, 5)
            fragmentIngress()}
        cell_46.setOnClickListener {
            submitCellNumber(it, 4, 6)
            fragmentIngress()}
        cell_47.setOnClickListener {
            submitCellNumber(it, 4, 7)
            fragmentIngress()}
        cell_48.setOnClickListener {
            submitCellNumber(it, 4, 8)
            fragmentIngress()}
        cell_49.setOnClickListener {
            submitCellNumber(it, 4, 9)
            fragmentIngress()}
        cell_51.setOnClickListener {
            submitCellNumber(it, 5, 1)
            fragmentIngress()}
        cell_52.setOnClickListener {
            submitCellNumber(it, 5, 2)
            fragmentIngress()}
        cell_53.setOnClickListener {
            submitCellNumber(it, 5, 3)
            fragmentIngress()}
        cell_54.setOnClickListener {
            submitCellNumber(it, 5, 4)
            fragmentIngress()}
        cell_55.setOnClickListener {
            submitCellNumber(it, 5, 5)
            fragmentIngress()}
        cell_56.setOnClickListener {
            submitCellNumber(it, 5, 6)
            fragmentIngress()}
        cell_57.setOnClickListener {
            submitCellNumber(it, 5, 7)
            fragmentIngress()}
        cell_58.setOnClickListener {
            submitCellNumber(it, 5, 8)
            fragmentIngress()}
        cell_59.setOnClickListener {
            submitCellNumber(it, 5, 9)
            fragmentIngress()}
        cell_61.setOnClickListener {
            submitCellNumber(it, 6, 1)
            fragmentIngress()}
        cell_62.setOnClickListener {
            submitCellNumber(it, 6, 2)
            fragmentIngress()}
        cell_63.setOnClickListener {
            submitCellNumber(it, 6, 3)
            fragmentIngress()}
        cell_64.setOnClickListener {
            submitCellNumber(it, 6, 4)
            fragmentIngress()}
        cell_65.setOnClickListener {
            submitCellNumber(it, 6, 5)
            fragmentIngress()}
        cell_66.setOnClickListener {
            submitCellNumber(it, 6, 6)
            fragmentIngress()}
        cell_67.setOnClickListener {
            submitCellNumber(it, 6, 7)
            fragmentIngress()}
        cell_68.setOnClickListener {
            submitCellNumber(it, 6, 8)
            fragmentIngress()}
        cell_69.setOnClickListener {
            submitCellNumber(it, 6, 9)
            fragmentIngress()}
        cell_71.setOnClickListener {
            submitCellNumber(it, 7, 1)
            fragmentIngress()}
        cell_72.setOnClickListener {
            submitCellNumber(it, 7, 2)
            fragmentIngress()}
        cell_73.setOnClickListener {
            submitCellNumber(it, 7, 3)
            fragmentIngress()}
        cell_74.setOnClickListener {
            submitCellNumber(it, 7, 4)
            fragmentIngress()}
        cell_75.setOnClickListener {
            submitCellNumber(it, 7, 5)
            fragmentIngress()}
        cell_76.setOnClickListener {
            submitCellNumber(it, 7, 6)
            fragmentIngress()}
        cell_77.setOnClickListener {
            submitCellNumber(it, 7, 7)
            fragmentIngress()}
        cell_78.setOnClickListener {
            submitCellNumber(it, 7, 8)
            fragmentIngress()}
        cell_79.setOnClickListener {
            submitCellNumber(it, 7, 9)
            fragmentIngress()}
        cell_81.setOnClickListener {
            submitCellNumber(it, 8, 1)
            fragmentIngress()}
        cell_82.setOnClickListener {
            submitCellNumber(it, 8, 2)
            fragmentIngress()}
        cell_83.setOnClickListener {
            submitCellNumber(it, 8, 3)
            fragmentIngress()}
        cell_84.setOnClickListener {
            submitCellNumber(it, 8, 4)
            fragmentIngress()}
        cell_85.setOnClickListener {
            submitCellNumber(it, 8, 5)
            fragmentIngress()}
        cell_86.setOnClickListener {
            submitCellNumber(it, 8, 6)
            fragmentIngress()}
        cell_87.setOnClickListener {
            submitCellNumber(it, 8, 7)
            fragmentIngress()}
        cell_88.setOnClickListener {
            submitCellNumber(it, 8, 8)
            fragmentIngress()}
        cell_89.setOnClickListener {
            submitCellNumber(it, 8, 9)
            fragmentIngress()}
        cell_91.setOnClickListener {
            submitCellNumber(it, 9, 1)
            fragmentIngress()}
        cell_92.setOnClickListener {
            submitCellNumber(it, 9, 2)
            fragmentIngress()}
        cell_93.setOnClickListener {
            submitCellNumber(it, 9, 3)
            fragmentIngress()}
        cell_94.setOnClickListener {
            submitCellNumber(it, 9, 4)
            fragmentIngress()}
        cell_95.setOnClickListener {
            submitCellNumber(it, 9, 5)
            fragmentIngress()}
        cell_96.setOnClickListener {
            submitCellNumber(it, 9, 6)
            fragmentIngress()}
        cell_97.setOnClickListener {
            submitCellNumber(it, 9, 7)
            fragmentIngress()}
        cell_98.setOnClickListener {
            submitCellNumber(it, 9, 8)
            fragmentIngress()}
        cell_99.setOnClickListener {
            submitCellNumber(it, 9, 9)
            fragmentIngress()}
    }

    fun submitCellNumber(view : View, row : Int, col : Int){
        rowCtx = row - 1
        colCtx = col - 1
        cellCtx = findViewById(view.id)
        cellCtx.setTextColor(Color.BLACK)
        cellCtx.background = getDrawable(R.drawable.cell_marked)
    }

    fun radialOne(view : View){
        game[rowCtx][colCtx] = 1
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialTwo(view : View){
        game[rowCtx][colCtx] = 2
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialThree(view : View){
        game[rowCtx][colCtx] = 3
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialFour(view : View){
        game[rowCtx][colCtx] = 4
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialFive(view : View){
        game[rowCtx][colCtx] = 5
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialSix(view : View){
        game[rowCtx][colCtx] = 6
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialSeven(view : View){
        game[rowCtx][colCtx] = 7
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialEight(view : View){
        game[rowCtx][colCtx] = 8
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialNine(view : View){
        game[rowCtx][colCtx] = 9
        cellCtx.text = game[rowCtx][colCtx].toString()
        fragmentEgress()
    }

    fun radialDelete(view : View){
        game[rowCtx][colCtx] = 0
        cellCtx.text = " "
        fragmentEgress()
    }

    fun buttonValidate(view : View){
        if(sudoku.validateBoard(game)){
            main_constraint_layout.setBackgroundColor(Color.GREEN)
        }else{
            main_constraint_layout.setBackgroundColor(Color.RED)
        }
    }

    fun checkIfZero(v : Int) : String{
        if(v > 0){
            return v.toString()
        }else{
            return " "
        }
    }

}
