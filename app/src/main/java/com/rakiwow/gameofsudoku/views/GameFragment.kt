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
import kotlinx.android.synthetic.main.activity_main.main_constraint_layout
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
    lateinit var cellCtx: TextView
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

    //TODO Lock initial clues of the sudoku puzzle, and also change the color of them
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
            game = sudoku.createGame(10)
        }
    }

    fun fragmentIngress() {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentViewGroup, numberFragment)
        fragmentTransaction.commit()
        main_constraint_layout.setBackgroundColor(resources.getColor(R.color.main_background))
    }

    fun fragmentEgress(number: Int) {

        if(number == 0){
            game[rowCtx][colCtx] = number
            cellCtx.text = " "
        }else if(number in 0..9) {
            game[rowCtx][colCtx] = number
            cellCtx.text = "$number"
        }

        childFragmentManager.beginTransaction().remove(numberFragment).commit()
        cellCtx.setBackgroundColor(resources.getColor(R.color.cellDefault))
        if(sudoku.isGridFilled(game)){
            if (sudoku.validateBoard(game)) {
                main_constraint_layout.setBackgroundColor(Color.GREEN)
            } else {
                main_constraint_layout.setBackgroundColor(Color.RED)
            }
        }
    }

    override fun onNumberSelect(number: Int) {
        fragmentEgress(number)
    }

    fun sudokuOnClickListeners() {
        //TODO "Fragment already added" error when tapping fast on a cell
        cell_11.setOnClickListener { submitCellNumber(it, 1, 1) }
        cell_12.setOnClickListener { submitCellNumber(it, 2, 1) }
        cell_13.setOnClickListener { submitCellNumber(it, 3, 1) }
        cell_14.setOnClickListener { submitCellNumber(it, 4, 1) }
        cell_15.setOnClickListener { submitCellNumber(it, 5, 1) }
        cell_16.setOnClickListener { submitCellNumber(it, 6, 1) }
        cell_17.setOnClickListener { submitCellNumber(it, 7, 1) }
        cell_18.setOnClickListener { submitCellNumber(it, 8, 1) }
        cell_19.setOnClickListener { submitCellNumber(it, 9, 1) }
        cell_21.setOnClickListener { submitCellNumber(it, 1, 2) }
        cell_22.setOnClickListener { submitCellNumber(it, 2, 2) }
        cell_23.setOnClickListener { submitCellNumber(it, 3, 2) }
        cell_24.setOnClickListener { submitCellNumber(it, 4, 2) }
        cell_25.setOnClickListener { submitCellNumber(it, 5, 2) }
        cell_26.setOnClickListener { submitCellNumber(it, 6, 2) }
        cell_27.setOnClickListener { submitCellNumber(it, 7, 2) }
        cell_28.setOnClickListener { submitCellNumber(it, 8, 2) }
        cell_29.setOnClickListener { submitCellNumber(it, 9, 2) }
        cell_31.setOnClickListener { submitCellNumber(it, 1, 3) }
        cell_32.setOnClickListener { submitCellNumber(it, 2, 3) }
        cell_33.setOnClickListener { submitCellNumber(it, 3, 3) }
        cell_34.setOnClickListener { submitCellNumber(it, 4, 3) }
        cell_35.setOnClickListener { submitCellNumber(it, 5, 3) }
        cell_36.setOnClickListener { submitCellNumber(it, 6, 3) }
        cell_37.setOnClickListener { submitCellNumber(it, 7, 3) }
        cell_38.setOnClickListener { submitCellNumber(it, 8, 3) }
        cell_39.setOnClickListener { submitCellNumber(it, 9, 3) }
        cell_41.setOnClickListener { submitCellNumber(it, 1, 4) }
        cell_42.setOnClickListener { submitCellNumber(it, 2, 4) }
        cell_43.setOnClickListener { submitCellNumber(it, 3, 4) }
        cell_44.setOnClickListener { submitCellNumber(it, 4, 4) }
        cell_45.setOnClickListener { submitCellNumber(it, 5, 4) }
        cell_46.setOnClickListener { submitCellNumber(it, 6, 4) }
        cell_47.setOnClickListener { submitCellNumber(it, 7, 4) }
        cell_48.setOnClickListener { submitCellNumber(it, 8, 4) }
        cell_49.setOnClickListener { submitCellNumber(it, 9, 4) }
        cell_51.setOnClickListener { submitCellNumber(it, 1, 5) }
        cell_52.setOnClickListener { submitCellNumber(it, 2, 5) }
        cell_53.setOnClickListener { submitCellNumber(it, 3, 5) }
        cell_54.setOnClickListener { submitCellNumber(it, 4, 5) }
        cell_55.setOnClickListener { submitCellNumber(it, 5, 5) }
        cell_56.setOnClickListener { submitCellNumber(it, 6, 5) }
        cell_57.setOnClickListener { submitCellNumber(it, 7, 5) }
        cell_58.setOnClickListener { submitCellNumber(it, 8, 5) }
        cell_59.setOnClickListener { submitCellNumber(it, 9, 5) }
        cell_61.setOnClickListener { submitCellNumber(it, 1, 6) }
        cell_62.setOnClickListener { submitCellNumber(it, 2, 6) }
        cell_63.setOnClickListener { submitCellNumber(it, 3, 6) }
        cell_64.setOnClickListener { submitCellNumber(it, 4, 6) }
        cell_65.setOnClickListener { submitCellNumber(it, 5, 6) }
        cell_66.setOnClickListener { submitCellNumber(it, 6, 6) }
        cell_67.setOnClickListener { submitCellNumber(it, 7, 6) }
        cell_68.setOnClickListener { submitCellNumber(it, 8, 6) }
        cell_69.setOnClickListener { submitCellNumber(it, 9, 6) }
        cell_71.setOnClickListener { submitCellNumber(it, 1, 7) }
        cell_72.setOnClickListener { submitCellNumber(it, 2, 7) }
        cell_73.setOnClickListener { submitCellNumber(it, 3, 7) }
        cell_74.setOnClickListener { submitCellNumber(it, 4, 7) }
        cell_75.setOnClickListener { submitCellNumber(it, 5, 7) }
        cell_76.setOnClickListener { submitCellNumber(it, 6, 7) }
        cell_77.setOnClickListener { submitCellNumber(it, 7, 7) }
        cell_78.setOnClickListener { submitCellNumber(it, 8, 7) }
        cell_79.setOnClickListener { submitCellNumber(it, 9, 7) }
        cell_81.setOnClickListener { submitCellNumber(it, 1, 8) }
        cell_82.setOnClickListener { submitCellNumber(it, 2, 8) }
        cell_83.setOnClickListener { submitCellNumber(it, 3, 8) }
        cell_84.setOnClickListener { submitCellNumber(it, 4, 8) }
        cell_85.setOnClickListener { submitCellNumber(it, 5, 8) }
        cell_86.setOnClickListener { submitCellNumber(it, 6, 8) }
        cell_87.setOnClickListener { submitCellNumber(it, 7, 8) }
        cell_88.setOnClickListener { submitCellNumber(it, 8, 8) }
        cell_89.setOnClickListener { submitCellNumber(it, 9, 8) }
        cell_91.setOnClickListener { submitCellNumber(it, 1, 9) }
        cell_92.setOnClickListener { submitCellNumber(it, 2, 9) }
        cell_93.setOnClickListener { submitCellNumber(it, 3, 9) }
        cell_94.setOnClickListener { submitCellNumber(it, 4, 9) }
        cell_95.setOnClickListener { submitCellNumber(it, 5, 9) }
        cell_96.setOnClickListener { submitCellNumber(it, 6, 9) }
        cell_97.setOnClickListener { submitCellNumber(it, 7, 9) }
        cell_98.setOnClickListener { submitCellNumber(it, 8, 9) }
        cell_99.setOnClickListener { submitCellNumber(it, 9, 9) }
    }

    fun submitCellNumber(view: View, row: Int, col: Int) {
        rowCtx = row - 1
        colCtx = col - 1
        cellCtx = activity!!.findViewById(view.id)
        cellCtx.setBackgroundColor(resources.getColor(R.color.cellMarked))
        fragmentIngress()
    }

    fun initCell(tv: TextView, n: Int) {
        tv.setTextColor(Color.BLACK)
        if (n > 0) {
            tv.setBackgroundColor(resources.getColor(R.color.cellClue))
            tv.setOnClickListener { } //This disables the onClickListener
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