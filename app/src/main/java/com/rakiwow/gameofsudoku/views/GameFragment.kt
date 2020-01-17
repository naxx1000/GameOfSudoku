package com.rakiwow.gameofsudoku.views

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.utils.MySudoku
import java.util.*
import android.graphics.Color
import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.utils.CellTextView
import com.rakiwow.gameofsudoku.viewmodel.HistoryViewModel
import com.rakiwow.gameofsudoku.viewmodel.MainSharedViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.coroutines.*
import java.lang.Exception

class GameFragment : Fragment(), NumberPickerFragment.OnNumberSelectListener {

    val sudoku: MySudoku = MySudoku()
    val numberFragment = NumberPickerFragment()
    val FRAGMENT_ID = 2 //Gamefragment will have 2 as the ID

    var isPuzzleComplete: Boolean = false
    var rowCtx: Int = 0
    var colCtx: Int = 0
    private var gameDifficulty = -1
    var pauseOffset: Long = 0L
    var hasRadialFragmentLaunched = false
    var isChronometerRunning = false
    lateinit var cellCtx: CellTextView
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var sharedViewModel: MainSharedViewModel
    var clues = 0
    var game: Array<IntArray> = Array(9) { IntArray(9) }
    var unsolvedGame: Array<IntArray> = Array(9) { IntArray(9) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        sharedViewModel = activity?.run{
            ViewModelProvider(this).get(MainSharedViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        sharedViewModel.currentFragment = FRAGMENT_ID
        gameDifficulty = arguments!!.getInt("difficulty", -1)

        createPuzzle(gameDifficulty)

        initGameLayout()
        initLayoutColors()
    }

    //Inserts the values from the grid into each Cell Text View
    fun setUpGrid() {
        //Row 1
        initCell(cell_11, 0, 0)
        initCell(cell_21, 0, 1)
        initCell(cell_31, 0, 2)
        initCell(cell_41, 0, 3)
        initCell(cell_51, 0, 4)
        initCell(cell_61, 0, 5)
        initCell(cell_71, 0, 6)
        initCell(cell_81, 0, 7)
        initCell(cell_91, 0, 8)

        //Row 2
        initCell(cell_12, 1, 0)
        initCell(cell_22, 1, 1)
        initCell(cell_32, 1, 2)
        initCell(cell_42, 1, 3)
        initCell(cell_52, 1, 4)
        initCell(cell_62, 1, 5)
        initCell(cell_72, 1, 6)
        initCell(cell_82, 1, 7)
        initCell(cell_92, 1, 8)

        //Row 3
        initCell(cell_13, 2, 0)
        initCell(cell_23, 2, 1)
        initCell(cell_33, 2, 2)
        initCell(cell_43, 2, 3)
        initCell(cell_53, 2, 4)
        initCell(cell_63, 2, 5)
        initCell(cell_73, 2, 6)
        initCell(cell_83, 2, 7)
        initCell(cell_93, 2, 8)

        //Row 4
        initCell(cell_14, 3, 0)
        initCell(cell_24, 3, 1)
        initCell(cell_34, 3, 2)
        initCell(cell_44, 3, 3)
        initCell(cell_54, 3, 4)
        initCell(cell_64, 3, 5)
        initCell(cell_74, 3, 6)
        initCell(cell_84, 3, 7)
        initCell(cell_94, 3, 8)

        //Row 5
        initCell(cell_15, 4, 0)
        initCell(cell_25, 4, 1)
        initCell(cell_35, 4, 2)
        initCell(cell_45, 4, 3)
        initCell(cell_55, 4, 4)
        initCell(cell_65, 4, 5)
        initCell(cell_75, 4, 6)
        initCell(cell_85, 4, 7)
        initCell(cell_95, 4, 8)

        //Row 6
        initCell(cell_16, 5, 0)
        initCell(cell_26, 5, 1)
        initCell(cell_36, 5, 2)
        initCell(cell_46, 5, 3)
        initCell(cell_56, 5, 4)
        initCell(cell_66, 5, 5)
        initCell(cell_76, 5, 6)
        initCell(cell_86, 5, 7)
        initCell(cell_96, 5, 8)

        //Row 7
        initCell(cell_17, 6, 0)
        initCell(cell_27, 6, 1)
        initCell(cell_37, 6, 2)
        initCell(cell_47, 6, 3)
        initCell(cell_57, 6, 4)
        initCell(cell_67, 6, 5)
        initCell(cell_77, 6, 6)
        initCell(cell_87, 6, 7)
        initCell(cell_97, 6, 8)

        //Row 8
        initCell(cell_18, 7, 0)
        initCell(cell_28, 7, 1)
        initCell(cell_38, 7, 2)
        initCell(cell_48, 7, 3)
        initCell(cell_58, 7, 4)
        initCell(cell_68, 7, 5)
        initCell(cell_78, 7, 6)
        initCell(cell_88, 7, 7)
        initCell(cell_98, 7, 8)

        //Row 9
        initCell(cell_19, 8, 0)
        initCell(cell_29, 8, 1)
        initCell(cell_39, 8, 2)
        initCell(cell_49, 8, 3)
        initCell(cell_59, 8, 4)
        initCell(cell_69, 8, 5)
        initCell(cell_79, 8, 6)
        initCell(cell_89, 8, 7)
        initCell(cell_99, 8, 8)

    }

    fun createPuzzle(difficulty: Int) {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        GlobalScope.launch(Dispatchers.Main) {
            if(difficulty == -1){ //If user has clicked continue
                val gridString = sharedPref.getString("game", "")
                val unsolvedGridString = sharedPref.getString("unsolved", "")
                if(isPuzzleComplete){
                    pauseChronometer()
                    progressBar?.visibility = View.VISIBLE
                    game = Array(9) { IntArray(9) }
                    unsolvedGame = Array(9) { IntArray(9) }
                    val asyncTask = async(Dispatchers.IO) {
                        createPuzzleBackground(difficulty)
                    }
                    asyncTask.await()
                    progressBar?.visibility = View.GONE
                    sudokuOnClickListeners(false)
                    for (i in 0 until 9) {
                        for (j in 0 until 9) {
                            unsolvedGame[i][j] =
                                game[i][j] //unsolvedGame = game, would for some reason bind it that reference
                        }
                    }
                    setUpGrid()
                    resetChronometer()
                    startChronometer()
                    isPuzzleComplete = false
                }else{ //If saved game does exist
                    val st1 = StringTokenizer(gridString, ",")
                    val st2 = StringTokenizer(unsolvedGridString, ",")
                    for (i in 0 until 9){
                        for (j in 0 until 9){
                            game[i][j] = st1.nextToken().toInt()
                            unsolvedGame[i][j] = st2.nextToken().toInt()
                        }
                    }
                    gameDifficulty = sharedPref.getInt("difficulty", 0)
                    clues = sharedPref.getInt("clues", 81)
                    sudokuOnClickListeners(false)
                    setUpGrid()
                }
            }else{ //Create new sudoku with a difficulty
                pauseChronometer()
                progressBar?.visibility = View.VISIBLE
                game = Array(9) { IntArray(9) }
                unsolvedGame = Array(9) { IntArray(9) }
                val asyncTask = async(Dispatchers.IO) {
                    createPuzzleBackground(difficulty)
                }
                asyncTask.await()
                progressBar?.visibility = View.GONE
                sudokuOnClickListeners(false)
                for (i in 0 until 9) {
                    for (j in 0 until 9) {
                        unsolvedGame[i][j] =
                            game[i][j] //unsolvedGame = game, would for some reason bind it that reference
                    }
                }
                setUpGrid()
                resetChronometer()
                startChronometer()
                isPuzzleComplete = false
            }
        }
    }

    suspend fun createPuzzleBackground(difficulty: Int) = coroutineScope {
        launch {
            // Difficulties range from 0-10
            val sudokuBoard = sudoku.createGame(difficulty)
            game = sudokuBoard.board
            clues = sudokuBoard.clues
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
    }

    fun fragmentEgress(number: Int, isMark: Boolean?) {
        if (hasRadialFragmentLaunched) {
            hasRadialFragmentLaunched = false
            if (isMark == null || isMark) {
                if (number == 0) {
                    cellCtx.removeAllMarks()
                } else if (number in 1..9) {
                    if (cellCtx.hasMark(number)) {
                        cellCtx.removeMark(number)
                    } else {
                        cellCtx.addMark(number)
                    }
                }
            } else {
                if (number == 0) {
                    game[rowCtx][colCtx] = number
                    cellCtx.text = " "
                    addCellListeners(
                        cellCtx,
                        rowCtx + 1,
                        colCtx + 1,
                    false) //Reapplies listeners when a number is removed from a cell
                } else if (number in 1..9) {
                    game[rowCtx][colCtx] = number
                    cellCtx.text = "$number"
                    cellCtx.removeAllMarks()
                    cellCtx.setOnLongClickListener { false } //Removes long click listener on cell that already contains a number
                }
            }

            childFragmentManager.beginTransaction().remove(numberFragment).commit()
            cellCtx.setBackgroundColor(resources.getColor(R.color.cellDefault))
            //Check if user has completed the puzzle
            if (sudoku.isGridFilled(game)) {
                if (sudoku.validateBoard(game)) {
                    main_constraint_layout.setBackgroundColor(resources.getColor(R.color.correctSudoku))
                    historyViewModel.insert(
                        SudokuStats(
                            0,
                            gameDifficulty,
                            Date().time,
                            (SystemClock.elapsedRealtime() - gameChronometer.base).div(1000).toInt(),
                            unsolvedGame,
                            clues
                        )
                    )
                    pauseChronometer()
                    game = Array(9) { IntArray(9) }
                    unsolvedGame = Array(9) { IntArray(9) }
                    sudokuOnClickListeners(true)
                    isPuzzleComplete = true
                } else {
                    main_constraint_layout.setBackgroundColor(resources.getColor(R.color.wrongSudoku))
                }
            }
        }
    }

    override fun onNumberSelect(number: Int, isMark: Boolean?) {
        fragmentEgress(number, isMark)
    }

    fun sudokuOnClickListeners(omitListener: Boolean) {
        addCellListeners(cell_11, 1, 1, omitListener)
        addCellListeners(cell_12, 2, 1, omitListener)
        addCellListeners(cell_13, 3, 1, omitListener)
        addCellListeners(cell_14, 4, 1, omitListener)
        addCellListeners(cell_15, 5, 1, omitListener)
        addCellListeners(cell_16, 6, 1, omitListener)
        addCellListeners(cell_17, 7, 1, omitListener)
        addCellListeners(cell_18, 8, 1, omitListener)
        addCellListeners(cell_19, 9, 1, omitListener)
        addCellListeners(cell_21, 1, 2, omitListener)
        addCellListeners(cell_22, 2, 2, omitListener)
        addCellListeners(cell_23, 3, 2, omitListener)
        addCellListeners(cell_24, 4, 2, omitListener)
        addCellListeners(cell_25, 5, 2, omitListener)
        addCellListeners(cell_26, 6, 2, omitListener)
        addCellListeners(cell_27, 7, 2, omitListener)
        addCellListeners(cell_28, 8, 2, omitListener)
        addCellListeners(cell_29, 9, 2, omitListener)
        addCellListeners(cell_31, 1, 3, omitListener)
        addCellListeners(cell_32, 2, 3, omitListener)
        addCellListeners(cell_33, 3, 3, omitListener)
        addCellListeners(cell_34, 4, 3, omitListener)
        addCellListeners(cell_35, 5, 3, omitListener)
        addCellListeners(cell_36, 6, 3, omitListener)
        addCellListeners(cell_37, 7, 3, omitListener)
        addCellListeners(cell_38, 8, 3, omitListener)
        addCellListeners(cell_39, 9, 3, omitListener)
        addCellListeners(cell_41, 1, 4, omitListener)
        addCellListeners(cell_42, 2, 4, omitListener)
        addCellListeners(cell_43, 3, 4, omitListener)
        addCellListeners(cell_44, 4, 4, omitListener)
        addCellListeners(cell_45, 5, 4, omitListener)
        addCellListeners(cell_46, 6, 4, omitListener)
        addCellListeners(cell_47, 7, 4, omitListener)
        addCellListeners(cell_48, 8, 4, omitListener)
        addCellListeners(cell_49, 9, 4, omitListener)
        addCellListeners(cell_51, 1, 5, omitListener)
        addCellListeners(cell_52, 2, 5, omitListener)
        addCellListeners(cell_53, 3, 5, omitListener)
        addCellListeners(cell_54, 4, 5, omitListener)
        addCellListeners(cell_55, 5, 5, omitListener)
        addCellListeners(cell_56, 6, 5, omitListener)
        addCellListeners(cell_57, 7, 5, omitListener)
        addCellListeners(cell_58, 8, 5, omitListener)
        addCellListeners(cell_59, 9, 5, omitListener)
        addCellListeners(cell_61, 1, 6, omitListener)
        addCellListeners(cell_62, 2, 6, omitListener)
        addCellListeners(cell_63, 3, 6, omitListener)
        addCellListeners(cell_64, 4, 6, omitListener)
        addCellListeners(cell_65, 5, 6, omitListener)
        addCellListeners(cell_66, 6, 6, omitListener)
        addCellListeners(cell_67, 7, 6, omitListener)
        addCellListeners(cell_68, 8, 6, omitListener)
        addCellListeners(cell_69, 9, 6, omitListener)
        addCellListeners(cell_71, 1, 7, omitListener)
        addCellListeners(cell_72, 2, 7, omitListener)
        addCellListeners(cell_73, 3, 7, omitListener)
        addCellListeners(cell_74, 4, 7, omitListener)
        addCellListeners(cell_75, 5, 7, omitListener)
        addCellListeners(cell_76, 6, 7, omitListener)
        addCellListeners(cell_77, 7, 7, omitListener)
        addCellListeners(cell_78, 8, 7, omitListener)
        addCellListeners(cell_79, 9, 7, omitListener)
        addCellListeners(cell_81, 1, 8, omitListener)
        addCellListeners(cell_82, 2, 8, omitListener)
        addCellListeners(cell_83, 3, 8, omitListener)
        addCellListeners(cell_84, 4, 8, omitListener)
        addCellListeners(cell_85, 5, 8, omitListener)
        addCellListeners(cell_86, 6, 8, omitListener)
        addCellListeners(cell_87, 7, 8, omitListener)
        addCellListeners(cell_88, 8, 8, omitListener)
        addCellListeners(cell_89, 9, 8, omitListener)
        addCellListeners(cell_91, 1, 9, omitListener)
        addCellListeners(cell_92, 2, 9, omitListener)
        addCellListeners(cell_93, 3, 9, omitListener)
        addCellListeners(cell_94, 4, 9, omitListener)
        addCellListeners(cell_95, 5, 9, omitListener)
        addCellListeners(cell_96, 6, 9, omitListener)
        addCellListeners(cell_97, 7, 9, omitListener)
        addCellListeners(cell_98, 8, 9, omitListener)
        addCellListeners(cell_99, 9, 9, omitListener)
    }

    fun addCellListeners(view: View?, row: Int, col: Int, omitListener: Boolean) {
        if(!omitListener){
            view?.setOnClickListener { submitCellNumber(it, row, col, false) }
            view?.setOnLongClickListener { submitCellMark(it, row, col, true) }
        }else{
            view?.setOnClickListener{}
            view?.setOnLongClickListener {false}
        }
    }

    fun submitCellNumber(view: View, row: Int, col: Int, isMark: Boolean) {
        if (!hasRadialFragmentLaunched) {
            hasRadialFragmentLaunched = true
            rowCtx = row - 1
            colCtx = col - 1
            cellCtx = activity!!.findViewById(view.id)
            cellCtx.setBackgroundColor(resources.getColor(R.color.cellMarked))
            fragmentIngress(isMark)
        }
    }

    //A copy of submitCellNumber above, but needs to return true so the onClick does not fire
    fun submitCellMark(view: View, row: Int, col: Int, isMark: Boolean): Boolean {
        if (!hasRadialFragmentLaunched) {
            hasRadialFragmentLaunched = true
            rowCtx = row - 1
            colCtx = col - 1
            cellCtx = activity!!.findViewById(view.id)
            cellCtx.setBackgroundColor(resources.getColor(R.color.cellMarked))
            fragmentIngress(isMark)
            return true
        }
        return false
    }

    fun initCell(tv: CellTextView?, x: Int, y: Int) {
        tv?.setTextColor(Color.BLACK)
        tv?.removeAllMarks()
        val n = game[x][y]
        if (n > 0) {
            if(n == unsolvedGame[x][y]){
                tv?.setBackgroundColor(resources.getColor(R.color.cellClue))
                tv?.setOnClickListener { } //This disables the onClickListener
                tv?.setOnLongClickListener { false }
                tv?.text = n.toString()
            }else{
                tv?.setBackgroundColor(resources.getColor(R.color.cellDefault))
                tv?.text = n.toString()
            }
        } else {
            tv?.setBackgroundColor(resources.getColor(R.color.cellDefault))
            tv?.text = " "
        }
    }

    //Start chronometer
    fun startChronometer() {
        if (!isChronometerRunning) {
            gameChronometer.base = SystemClock.elapsedRealtime()
            gameChronometer.start()
            isChronometerRunning = true
        }
    }

    fun pauseChronometer() {
        if (isChronometerRunning) {
            gameChronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - gameChronometer.base
            isChronometerRunning = false
        }
    }

    fun resumeChronometer() {
        if (!isChronometerRunning) {
            val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
            pauseOffset = sharedPref.getLong("pause_offset", 0)
            gameChronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            gameChronometer.start()
            isChronometerRunning = true
        }
    }

    fun resetChronometer() {
        gameChronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
    }

    override fun onPause() {
        pauseChronometer()
        val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            val sb1 = StringBuilder()
            val sb2 = StringBuilder()
            for (i in 0 until 9){
                for (j in 0 until 9){
                    sb1.append(game[i][j].toString() + ",")
                    sb2.append(unsolvedGame[i][j].toString() + ",")
                }
            }
            putBoolean("isPuzzleComplete", isPuzzleComplete)
            putString("game", sb1.toString())
            putString("unsolved", sb2.toString())
            putInt("difficulty", gameDifficulty)
            putInt("clues", clues)
            putLong("pause_offset", pauseOffset)
            commit()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        resumeChronometer()
        val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        isPuzzleComplete = sharedPref.getBoolean("isPuzzleComplete", false)
    }

    fun initGameLayout() {
        mainGrid.post {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //Gridlayout
                val gOldParams = mainGrid.layoutParams as ConstraintLayout.LayoutParams
                val gNewParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    main_constraint_layout.width
                )
                gNewParams.startToStart = gOldParams.startToStart
                gNewParams.endToEnd = gOldParams.endToEnd
                gNewParams.topToTop = gOldParams.topToTop
                gNewParams.bottomToBottom = gOldParams.bottomToBottom
                mainGrid.layoutParams = gNewParams

                //Chronometer
                val constraintSet = ConstraintSet()
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.BOTTOM,
                    mainGrid.id,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.RIGHT
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.applyTo(main_constraint_layout)

            } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //Gridlayout
                val oldParams = mainGrid.layoutParams as ConstraintLayout.LayoutParams
                val newParams = ConstraintLayout.LayoutParams(
                    main_constraint_layout.height,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                newParams.startToStart = oldParams.startToStart
                newParams.endToEnd = oldParams.endToEnd
                newParams.topToTop = oldParams.topToTop
                newParams.bottomToBottom = oldParams.bottomToBottom
                mainGrid.layoutParams = newParams

                //Chronometer
                val constraintSet = ConstraintSet()
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.RIGHT,
                    mainGrid.id,
                    ConstraintSet.LEFT
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                constraintSet.connect(
                    gameChronometer.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
                constraintSet.applyTo(main_constraint_layout)
            }
        }
    }

    fun initLayoutColors() {
        sharedViewModel.colors.observe(viewLifecycleOwner, androidx.lifecycle.Observer<IntArray>{ colors ->
            main_constraint_layout.setBackgroundColor(colors[1])
            gameChronometer.setTextColor(colors[4])
        })
    }
}