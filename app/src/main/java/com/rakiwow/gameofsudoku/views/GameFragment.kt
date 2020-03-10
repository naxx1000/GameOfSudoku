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
import android.widget.Chronometer
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
    val FRAGMENT_ID = 1 //Gamefragment will have 2 as the ID

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

        gameChronometer.setOnChronometerTickListener {
            if((SystemClock.elapsedRealtime() - gameChronometer.base).div(1000).toInt() % 10 == 0){
                println((SystemClock.elapsedRealtime() - gameChronometer.base).div(1000).toInt())
            }
        }

        initGameLayout()
    }

    fun createPuzzle(difficulty: Int?) {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        GlobalScope.launch(Dispatchers.Main) {
            if(difficulty == -1){ //If a game is continued
                val gridString = sharedPref.getString("game", "")
                val unsolvedGridString = sharedPref.getString("unsolved", "")
                if(isPuzzleComplete){
                    gameDifficulty = sharedPref.getInt("difficulty", -1)
                    println("Creating new game from solved sudoku with difficulty " + gameDifficulty)
                    pauseChronometer()
                    progressBar?.visibility = View.VISIBLE
                    game = Array(9) { IntArray(9) }
                    unsolvedGame = Array(9) { IntArray(9) }
                    val asyncTask = async(Dispatchers.IO) {
                        createPuzzleBackground(gameDifficulty)
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
                    setUpGrid(true)
                    resetChronometer()
                    startChronometer()
                    isPuzzleComplete = false

                    println("game difficulty: " + gameDifficulty)
                    difficultyTextView.text = getDifficultyString(gameDifficulty)
                }else{ //If saved game does exist
                    gameDifficulty = sharedPref.getInt("difficulty", -1)
                    println("Continuing a previous sudoku with difficulty " + gameDifficulty)
                    val st1 = StringTokenizer(gridString, ",")
                    val st2 = StringTokenizer(unsolvedGridString, ",")
                    val st3 = sharedPref.getString("game_hints", "")?.split(";")
                    var markCounter = 0
                    for (i in 0 until 9){
                        for (j in 0 until 9){
                            game[i][j] = st1.nextToken().toInt()
                            unsolvedGame[i][j] = st2.nextToken().toInt()
                            if(st3?.get(markCounter) != "null"
                                && st3?.get(markCounter) != null
                                && st3[markCounter].isNotEmpty()){
                                val ctv = getCellAt(i + 1, j + 1)
                                ctv?.addSetOfMarks(st3[markCounter])
                            }
                            markCounter++
                        }
                    }
                    gameDifficulty = sharedPref.getInt("difficulty", 0)
                    clues = sharedPref.getInt("clues", 81)
                    sudokuOnClickListeners(false)
                    setUpGrid(false)

                    println("game difficulty: " + gameDifficulty)
                    difficultyTextView.text = getDifficultyString(gameDifficulty)
                }
            }else{ //Create new sudoku with a difficulty
                println("Creating a new sudoku with difficulty " + gameDifficulty)
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
                setUpGrid(true)
                resetChronometer()
                startChronometer()
                isPuzzleComplete = false

                println("game difficulty: " + gameDifficulty)
                difficultyTextView.text = getDifficultyString(gameDifficulty)
            }
        }
    }

    suspend fun createPuzzleBackground(difficulty: Int?) = coroutineScope {
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


    fun addCellListeners(row: Int, col: Int, omitListener: Boolean) {
        val view: CellTextView? = getCellAt(row, col)
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

    fun initCell(x: Int, y: Int, removeMark: Boolean) {
        val tv: CellTextView? = getCellAt(x + 1, y + 1)
        tv?.setTextColor(Color.BLACK)
        if(removeMark){
            tv?.removeAllMarks()
        }
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
        saveGameState()
        super.onPause()
    }

    private fun saveGameState() {
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.grid_layout_key),
            Context.MODE_PRIVATE
        ) ?: return
        with(sharedPref.edit()) {
            val sb1 = StringBuilder()
            val sb2 = StringBuilder()
            val sb3 = StringBuilder()
            for (i in 0 until 9) {
                for (j in 0 until 9) {
                    sb1.append(game[i][j].toString())
                    sb2.append(unsolvedGame[i][j].toString())
                    sb3.append(getCellAt(i + 1, j + 1)?.getSetOfMarks())
                    if (!(i == 8 && j == 8)) {
                        sb1.append(",")
                        sb2.append(",")
                        sb3.append(";")
                    }
                }
            }
            putBoolean("isPuzzleComplete", isPuzzleComplete)
            putString("game", sb1.toString())
            putString("unsolved", sb2.toString())
            putInt("difficulty", gameDifficulty)
            putInt("clues", clues)
            putLong("pause_offset", pauseOffset)
            putString("game_hints", sb3.toString())
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        resumeChronometer()
        val sharedPref = activity?.getSharedPreferences(getString(R.string.grid_layout_key), Context.MODE_PRIVATE) ?: return
        isPuzzleComplete = sharedPref.getBoolean("isPuzzleComplete", false)
    }

    private fun getCellAt(row: Int, col: Int): CellTextView? {
        val id = resources.getIdentifier("cell_" + col + row, "id", context?.packageName)
        return activity?.findViewById(id)
    }

    private fun getDifficultyString(difficulty: Int) : String{
        when(difficulty){
            0 -> return "I'M TOO YOUNG TO DIE"
            1 -> return "HURT ME PLENTY"
            2 -> return "ULTRA-VIOLENCE"
            3 -> return "NIGHTMARE"
            4 -> return "ULTRA-NIGHTMARE"
            404 -> return "TEST"
            -1 -> return "DEFAULT DIFFICULTY"
            else -> return "???"
        }
    }

    //Configures the layout to fit either portrait or landscape mode
    private fun initGameLayout() {
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

                val constraintSet = ConstraintSet()
                //Chronometer
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

                val constraintSet = ConstraintSet()
                //Chronometer
                constraintSet.connect(
                    gameInfoLayout.id,
                    ConstraintSet.RIGHT,
                    mainGrid.id,
                    ConstraintSet.LEFT
                )
                constraintSet.connect(
                    gameInfoLayout.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP
                )
                constraintSet.connect(
                    gameInfoLayout.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM
                )
                constraintSet.connect(
                    gameInfoLayout.id,
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.LEFT
                )
                constraintSet.applyTo(main_constraint_layout)
            }
        }
    }

    //Adds click listeners to all editable cells
    fun sudokuOnClickListeners(omitListener: Boolean) {
        addCellListeners(1, 1, omitListener)
        addCellListeners(2, 1, omitListener)
        addCellListeners(3, 1, omitListener)
        addCellListeners(4, 1, omitListener)
        addCellListeners(5, 1, omitListener)
        addCellListeners(6, 1, omitListener)
        addCellListeners(7, 1, omitListener)
        addCellListeners(8, 1, omitListener)
        addCellListeners(9, 1, omitListener)
        addCellListeners(1, 2, omitListener)
        addCellListeners(2, 2, omitListener)
        addCellListeners(3, 2, omitListener)
        addCellListeners(4, 2, omitListener)
        addCellListeners(5, 2, omitListener)
        addCellListeners(6, 2, omitListener)
        addCellListeners(7, 2, omitListener)
        addCellListeners(8, 2, omitListener)
        addCellListeners(9, 2, omitListener)
        addCellListeners(1, 3, omitListener)
        addCellListeners(2, 3, omitListener)
        addCellListeners(3, 3, omitListener)
        addCellListeners(4, 3, omitListener)
        addCellListeners(5, 3, omitListener)
        addCellListeners(6, 3, omitListener)
        addCellListeners(7, 3, omitListener)
        addCellListeners(8, 3, omitListener)
        addCellListeners(9, 3, omitListener)
        addCellListeners(1, 4, omitListener)
        addCellListeners(2, 4, omitListener)
        addCellListeners(3, 4, omitListener)
        addCellListeners(4, 4, omitListener)
        addCellListeners(5, 4, omitListener)
        addCellListeners(6, 4, omitListener)
        addCellListeners(7, 4, omitListener)
        addCellListeners(8, 4, omitListener)
        addCellListeners(9, 4, omitListener)
        addCellListeners(1, 5, omitListener)
        addCellListeners(2, 5, omitListener)
        addCellListeners(3, 5, omitListener)
        addCellListeners(4, 5, omitListener)
        addCellListeners(5, 5, omitListener)
        addCellListeners(6, 5, omitListener)
        addCellListeners(7, 5, omitListener)
        addCellListeners(8, 5, omitListener)
        addCellListeners(9, 5, omitListener)
        addCellListeners(1, 6, omitListener)
        addCellListeners(2, 6, omitListener)
        addCellListeners(3, 6, omitListener)
        addCellListeners(4, 6, omitListener)
        addCellListeners(5, 6, omitListener)
        addCellListeners(6, 6, omitListener)
        addCellListeners(7, 6, omitListener)
        addCellListeners(8, 6, omitListener)
        addCellListeners(9, 6, omitListener)
        addCellListeners(1, 7, omitListener)
        addCellListeners(2, 7, omitListener)
        addCellListeners(3, 7, omitListener)
        addCellListeners(4, 7, omitListener)
        addCellListeners(5, 7, omitListener)
        addCellListeners(6, 7, omitListener)
        addCellListeners(7, 7, omitListener)
        addCellListeners(8, 7, omitListener)
        addCellListeners(9, 7, omitListener)
        addCellListeners(1, 8, omitListener)
        addCellListeners(2, 8, omitListener)
        addCellListeners(3, 8, omitListener)
        addCellListeners(4, 8, omitListener)
        addCellListeners(5, 8, omitListener)
        addCellListeners(6, 8, omitListener)
        addCellListeners(7, 8, omitListener)
        addCellListeners(8, 8, omitListener)
        addCellListeners(9, 8, omitListener)
        addCellListeners(1, 9, omitListener)
        addCellListeners(2, 9, omitListener)
        addCellListeners(3, 9, omitListener)
        addCellListeners(4, 9, omitListener)
        addCellListeners(5, 9, omitListener)
        addCellListeners(6, 9, omitListener)
        addCellListeners(7, 9, omitListener)
        addCellListeners(8, 9, omitListener)
        addCellListeners(9, 9, omitListener)
    }

    //Inserts the values from the grid into each Cell Text View
    fun setUpGrid(removeMarks: Boolean) {
        //Row 1
        initCell(0, 0, removeMarks)
        initCell(0, 1, removeMarks)
        initCell(0, 2, removeMarks)
        initCell(0, 3, removeMarks)
        initCell(0, 4, removeMarks)
        initCell(0, 5, removeMarks)
        initCell(0, 6, removeMarks)
        initCell(0, 7, removeMarks)
        initCell(0, 8, removeMarks)

        //Row 2, removeMarks
        initCell(1, 0, removeMarks)
        initCell(1, 1, removeMarks)
        initCell(1, 2, removeMarks)
        initCell(1, 3, removeMarks)
        initCell(1, 4, removeMarks)
        initCell(1, 5, removeMarks)
        initCell(1, 6, removeMarks)
        initCell(1, 7, removeMarks)
        initCell(1, 8, removeMarks)

        //Row 3, removeMarks
        initCell(2, 0, removeMarks)
        initCell(2, 1, removeMarks)
        initCell(2, 2, removeMarks)
        initCell(2, 3, removeMarks)
        initCell(2, 4, removeMarks)
        initCell(2, 5, removeMarks)
        initCell(2, 6, removeMarks)
        initCell(2, 7, removeMarks)
        initCell(2, 8, removeMarks)

        //Row 4, removeMarks
        initCell(3, 0, removeMarks)
        initCell(3, 1, removeMarks)
        initCell(3, 2, removeMarks)
        initCell(3, 3, removeMarks)
        initCell(3, 4, removeMarks)
        initCell(3, 5, removeMarks)
        initCell(3, 6, removeMarks)
        initCell(3, 7, removeMarks)
        initCell(3, 8, removeMarks)

        //Row 5, removeMarks
        initCell(4, 0, removeMarks)
        initCell(4, 1, removeMarks)
        initCell(4, 2, removeMarks)
        initCell(4, 3, removeMarks)
        initCell(4, 4, removeMarks)
        initCell(4, 5, removeMarks)
        initCell(4, 6, removeMarks)
        initCell(4, 7, removeMarks)
        initCell(4, 8, removeMarks)

        //Row 6, removeMarks
        initCell(5, 0, removeMarks)
        initCell(5, 1, removeMarks)
        initCell(5, 2, removeMarks)
        initCell(5, 3, removeMarks)
        initCell(5, 4, removeMarks)
        initCell(5, 5, removeMarks)
        initCell(5, 6, removeMarks)
        initCell(5, 7, removeMarks)
        initCell(5, 8, removeMarks)

        //Row 7, removeMarks
        initCell(6, 0, removeMarks)
        initCell(6, 1, removeMarks)
        initCell(6, 2, removeMarks)
        initCell(6, 3, removeMarks)
        initCell(6, 4, removeMarks)
        initCell(6, 5, removeMarks)
        initCell(6, 6, removeMarks)
        initCell(6, 7, removeMarks)
        initCell(6, 8, removeMarks)

        //Row 8, removeMarks
        initCell(7, 0, removeMarks)
        initCell(7, 1, removeMarks)
        initCell(7, 2, removeMarks)
        initCell(7, 3, removeMarks)
        initCell(7, 4, removeMarks)
        initCell(7, 5, removeMarks)
        initCell(7, 6, removeMarks)
        initCell(7, 7, removeMarks)
        initCell(7, 8, removeMarks)

        //Row 9, removeMarks
        initCell(8, 0, removeMarks)
        initCell(8, 1, removeMarks)
        initCell(8, 2, removeMarks)
        initCell(8, 3, removeMarks)
        initCell(8, 4, removeMarks)
        initCell(8, 5, removeMarks)
        initCell(8, 6, removeMarks)
        initCell(8, 7, removeMarks)
        initCell(8, 8, removeMarks)

    }
}