package com.rakiwow.gameofsudoku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.utils.MySudoku
import java.util.*
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.rakiwow.gameofsudoku.utils.CellTextView
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.*

class GameFragment: Fragment(), NumberPickerFragment.OnNumberSelectListener{

    val sudoku: MySudoku = MySudoku()
    val numberFragment = NumberPickerFragment()
    val stopWatchTimer = Timer()

    var stopWatchSeconds: Int = 0
    var stopWatchMinutes: Int = 0
    var rowCtx: Int = 0
    var colCtx: Int = 0
    lateinit var cellCtx: CellTextView
    var game: Array<IntArray> = Array(9) { IntArray(9) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createPuzzle()

        startStopWatch()

        button_generate.setOnClickListener {
            createPuzzle()
        }
    }

    //Inserts the values from the grid into each Cell Text View
    fun setUpGrid(grid: Array<IntArray>) {
        //Row 1
        initCell(cell_11, grid[0][0])
        initCell(cell_21, grid[0][1])
        initCell(cell_31, grid[0][2])
        initCell(cell_41, grid[0][3])
        initCell(cell_51, grid[0][4])
        initCell(cell_61, grid[0][5])
        initCell(cell_71, grid[0][6])
        initCell(cell_81, grid[0][7])
        initCell(cell_91, grid[0][8])

        //Row 2
        initCell(cell_12, grid[1][0])
        initCell(cell_22, grid[1][1])
        initCell(cell_32, grid[1][2])
        initCell(cell_42, grid[1][3])
        initCell(cell_52, grid[1][4])
        initCell(cell_62, grid[1][5])
        initCell(cell_72, grid[1][6])
        initCell(cell_82, grid[1][7])
        initCell(cell_92, grid[1][8])

        //Row 3
        initCell(cell_13, grid[2][0])
        initCell(cell_23, grid[2][1])
        initCell(cell_33, grid[2][2])
        initCell(cell_43, grid[2][3])
        initCell(cell_53, grid[2][4])
        initCell(cell_63, grid[2][5])
        initCell(cell_73, grid[2][6])
        initCell(cell_83, grid[2][7])
        initCell(cell_93, grid[2][8])

        //Row 4
        initCell(cell_14, grid[3][0])
        initCell(cell_24, grid[3][1])
        initCell(cell_34, grid[3][2])
        initCell(cell_44, grid[3][3])
        initCell(cell_54, grid[3][4])
        initCell(cell_64, grid[3][5])
        initCell(cell_74, grid[3][6])
        initCell(cell_84, grid[3][7])
        initCell(cell_94, grid[3][8])

        //Row 5
        initCell(cell_15, grid[4][0])
        initCell(cell_25, grid[4][1])
        initCell(cell_35, grid[4][2])
        initCell(cell_45, grid[4][3])
        initCell(cell_55, grid[4][4])
        initCell(cell_65, grid[4][5])
        initCell(cell_75, grid[4][6])
        initCell(cell_85, grid[4][7])
        initCell(cell_95, grid[4][8])

        //Row 6
        initCell(cell_16, grid[5][0])
        initCell(cell_26, grid[5][1])
        initCell(cell_36, grid[5][2])
        initCell(cell_46, grid[5][3])
        initCell(cell_56, grid[5][4])
        initCell(cell_66, grid[5][5])
        initCell(cell_76, grid[5][6])
        initCell(cell_86, grid[5][7])
        initCell(cell_96, grid[5][8])

        //Row 7
        initCell(cell_17, grid[6][0])
        initCell(cell_27, grid[6][1])
        initCell(cell_37, grid[6][2])
        initCell(cell_47, grid[6][3])
        initCell(cell_57, grid[6][4])
        initCell(cell_67, grid[6][5])
        initCell(cell_77, grid[6][6])
        initCell(cell_87, grid[6][7])
        initCell(cell_97, grid[6][8])

        //Row 8
        initCell(cell_18, grid[7][0])
        initCell(cell_28, grid[7][1])
        initCell(cell_38, grid[7][2])
        initCell(cell_48, grid[7][3])
        initCell(cell_58, grid[7][4])
        initCell(cell_68, grid[7][5])
        initCell(cell_78, grid[7][6])
        initCell(cell_88, grid[7][7])
        initCell(cell_98, grid[7][8])

        //Row 9
        initCell(cell_19, grid[8][0])
        initCell(cell_29, grid[8][1])
        initCell(cell_39, grid[8][2])
        initCell(cell_49, grid[8][3])
        initCell(cell_59, grid[8][4])
        initCell(cell_69, grid[8][5])
        initCell(cell_79, grid[8][6])
        initCell(cell_89, grid[8][7])
        initCell(cell_99, grid[8][8])

    }

    fun createPuzzle() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            button_generate.visibility = View.INVISIBLE
            val asyncTask = async(Dispatchers.IO) {
                createPuzzleBackground()
            }
            asyncTask.await()
            progressBar.visibility = View.GONE
            button_generate.visibility = View.VISIBLE
            sudokuOnClickListeners()
            setUpGrid(game)
            stopWatchSeconds = 0
            stopWatchMinutes = 0
            textViewTimer.text = "00:00"
        }
    }

    suspend fun createPuzzleBackground() = coroutineScope {
        launch {
            // Difficulties range from 0-10
            //TODO weird pattern at easier difficulties
            game = sudoku.createGame(0)
        }
    }

    fun fragmentIngress(isMark: Boolean) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //Add arguments to the fragment, so it can detect if it is marking numbers or not
        val args = Bundle()
        args.putBoolean("isMark", isMark)
        args.putIntegerArrayList("markList", cellCtx.markList)
        numberFragment.arguments = args
        fragmentTransaction.add(R.id.fragmentViewGroup, numberFragment)
        fragmentTransaction.commit()
        main_constraint_layout.setBackgroundColor(resources.getColor(R.color.main_background))
    }

    fun fragmentEgress(number: Int, isMark: Boolean?) {

        if(isMark == null || isMark){
            if(number == 0){
                cellCtx.removeAllMarks()
            }else if(number in 1..9){
                if(cellCtx.hasMark(number)){
                    cellCtx.removeMark(number)
                }else{
                    println("mark added")
                    cellCtx.addMark(number)
                }
            }
        }else{
            if(number == 0){
                game[rowCtx][colCtx] = number
                cellCtx.text = " "
            }else if(number in 1..9) {
                game[rowCtx][colCtx] = number
                cellCtx.text = "$number"
                cellCtx.removeAllMarks()
            }
        }

        childFragmentManager.beginTransaction().remove(numberFragment).commit()
        cellCtx.setBackgroundColor(resources.getColor(R.color.cellDefault))
        if(sudoku.isGridFilled(game)){
            if (sudoku.validateBoard(game)) {
                main_constraint_layout.setBackgroundColor(Color.GREEN)
                stopWatchTimer.cancel()
            } else {
                main_constraint_layout.setBackgroundColor(Color.RED)
            }
        }
    }

    override fun onNumberSelect(number: Int, isMark: Boolean?) {
        fragmentEgress(number, isMark)
    }

    fun sudokuOnClickListeners() {
        //TODO "Fragment already added" error when tapping fast on a cell
        addCellListeners(cell_11, 1, 1)
        addCellListeners(cell_12, 2, 1)
        addCellListeners(cell_13, 3, 1)
        addCellListeners(cell_14, 4, 1)
        addCellListeners(cell_15, 5, 1)
        addCellListeners(cell_16, 6, 1)
        addCellListeners(cell_17, 7, 1)
        addCellListeners(cell_18, 8, 1)
        addCellListeners(cell_19, 9, 1)
        addCellListeners(cell_21, 1, 2)
        addCellListeners(cell_22, 2, 2)
        addCellListeners(cell_23, 3, 2)
        addCellListeners(cell_24, 4, 2)
        addCellListeners(cell_25, 5, 2)
        addCellListeners(cell_26, 6, 2)
        addCellListeners(cell_27, 7, 2)
        addCellListeners(cell_28, 8, 2)
        addCellListeners(cell_29, 9, 2)
        addCellListeners(cell_31, 1, 3)
        addCellListeners(cell_32, 2, 3)
        addCellListeners(cell_33, 3, 3)
        addCellListeners(cell_34, 4, 3)
        addCellListeners(cell_35, 5, 3)
        addCellListeners(cell_36, 6, 3)
        addCellListeners(cell_37, 7, 3)
        addCellListeners(cell_38, 8, 3)
        addCellListeners(cell_39, 9, 3)
        addCellListeners(cell_41, 1, 4)
        addCellListeners(cell_42, 2, 4)
        addCellListeners(cell_43, 3, 4)
        addCellListeners(cell_44, 4, 4)
        addCellListeners(cell_45, 5, 4)
        addCellListeners(cell_46, 6, 4)
        addCellListeners(cell_47, 7, 4)
        addCellListeners(cell_48, 8, 4)
        addCellListeners(cell_49, 9, 4)
        addCellListeners(cell_51, 1, 5)
        addCellListeners(cell_52, 2, 5)
        addCellListeners(cell_53, 3, 5)
        addCellListeners(cell_54, 4, 5)
        addCellListeners(cell_55, 5, 5)
        addCellListeners(cell_56, 6, 5)
        addCellListeners(cell_57, 7, 5)
        addCellListeners(cell_58, 8, 5)
        addCellListeners(cell_59, 9, 5)
        addCellListeners(cell_61, 1, 6)
        addCellListeners(cell_62, 2, 6)
        addCellListeners(cell_63, 3, 6)
        addCellListeners(cell_64, 4, 6)
        addCellListeners(cell_65, 5, 6)
        addCellListeners(cell_66, 6, 6)
        addCellListeners(cell_67, 7, 6)
        addCellListeners(cell_68, 8, 6)
        addCellListeners(cell_69, 9, 6)
        addCellListeners(cell_71, 1, 7)
        addCellListeners(cell_72, 2, 7)
        addCellListeners(cell_73, 3, 7)
        addCellListeners(cell_74, 4, 7)
        addCellListeners(cell_75, 5, 7)
        addCellListeners(cell_76, 6, 7)
        addCellListeners(cell_77, 7, 7)
        addCellListeners(cell_78, 8, 7)
        addCellListeners(cell_79, 9, 7)
        addCellListeners(cell_81, 1, 8)
        addCellListeners(cell_82, 2, 8)
        addCellListeners(cell_83, 3, 8)
        addCellListeners(cell_84, 4, 8)
        addCellListeners(cell_85, 5, 8)
        addCellListeners(cell_86, 6, 8)
        addCellListeners(cell_87, 7, 8)
        addCellListeners(cell_88, 8, 8)
        addCellListeners(cell_89, 9, 8)
        addCellListeners(cell_91, 1, 9)
        addCellListeners(cell_92, 2, 9)
        addCellListeners(cell_93, 3, 9)
        addCellListeners(cell_94, 4, 9)
        addCellListeners(cell_95, 5, 9)
        addCellListeners(cell_96, 6, 9)
        addCellListeners(cell_97, 7, 9)
        addCellListeners(cell_98, 8, 9)
        addCellListeners(cell_99, 9, 9)
    }

    fun addCellListeners(view: View, row: Int, col: Int){
        view.setOnClickListener { submitCellNumber(it, row, col, false) }
        view.setOnLongClickListener { submitCellMark(it, row, col, true) }
    }

    fun submitCellNumber(view: View, row: Int, col: Int, isMark: Boolean) {
        rowCtx = row - 1
        colCtx = col - 1
        cellCtx = activity!!.findViewById(view.id)
        cellCtx.setBackgroundColor(resources.getColor(R.color.cellMarked))
        fragmentIngress(isMark)
    }

    //A copy of submitCellNumber above, but needs to return true so the onClick does not fire
    fun submitCellMark(view: View, row: Int, col: Int, isMark: Boolean): Boolean {
        rowCtx = row - 1
        colCtx = col - 1
        cellCtx = activity!!.findViewById(view.id)
        cellCtx.setBackgroundColor(resources.getColor(R.color.cellMarked))
        fragmentIngress(isMark)
        return true
    }

    fun initCell(tv: CellTextView, n: Int) {
        tv.setTextColor(Color.BLACK)
        tv.removeAllMarks()
        if (n > 0) {
            tv.setBackgroundColor(resources.getColor(R.color.cellClue))
            tv.setOnClickListener { } //This disables the onClickListener
            tv.setOnLongClickListener { false }
            tv.text = n.toString()
        } else {
            tv.setBackgroundColor(resources.getColor(R.color.cellDefault))
            tv.text = " "
        }
    }

    //TODO Pause timer
    fun startStopWatch() {
        stopWatchTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    stopWatchSeconds++
                    if (stopWatchSeconds >= 60) {
                        stopWatchMinutes++
                        stopWatchSeconds = 0
                    }
                    var timerString = ""
                    if (stopWatchMinutes < 10) {
                        timerString += "0$stopWatchMinutes:"
                    } else {
                        timerString += "$stopWatchMinutes:"
                    }
                    if (stopWatchSeconds < 10) {
                        timerString += "0$stopWatchSeconds"
                    } else {
                        timerString += "$stopWatchSeconds"
                    }
                    textViewTimer.text = timerString
                }
            }
            , 1000, 1000
        )
    }
}