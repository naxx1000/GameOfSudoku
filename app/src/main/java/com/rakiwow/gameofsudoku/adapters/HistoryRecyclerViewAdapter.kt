package com.rakiwow.gameofsudoku.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.utils.SudokuCanvasView
import java.lang.StringBuilder
import java.text.DateFormat
import java.util.*

class HistoryRecyclerViewAdapter(locale: Locale) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.StatsViewHolder>() {

    private var statsList = emptyList<SudokuStats>()
    private val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale)

    inner class StatsViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val historyTextView: TextView = v.findViewById(R.id.history_textview)
        val historySudokuView: SudokuCanvasView = v.findViewById(R.id.history_sudokuview)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StatsViewHolder {
        return StatsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_adapter_history, parent, false))
    }

    override fun onBindViewHolder(
        holder: StatsViewHolder,
        position: Int
    ) {
        val statsItem = statsList[position]
        val timeMinutes = statsItem.completedTime?.div(60)
        val timeSeconds = statsItem.completedTime?.minus(statsItem.completedTime.div(60) * 60)
        val timeStringBuilder = StringBuilder()
        if(timeMinutes == 1){
            timeStringBuilder.append(timeMinutes.toString() + " Minute ")
        }else if(timeMinutes != 0) {
            timeStringBuilder.append(timeMinutes.toString() + " Minutes ")
        }
        if(timeSeconds == 1){
            timeStringBuilder.append(timeSeconds.toString() + " Second")
        }else if(timeSeconds != 0){
            timeStringBuilder.append(timeSeconds.toString() + " Seconds")
        }
        val date: String? = dateFormat.format(statsItem.date?.let { Date(it) })
        holder.historyTextView.text = "Difficulty: ${getDifficultyString(statsItem.difficulty)} // ${statsItem.clues} clues\nCompleted in: $timeStringBuilder\nDate: $date"
        holder.historySudokuView.grid = statsItem.grid

    }

    internal fun setStats(stats: List<SudokuStats>){
        this.statsList = stats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return statsList.size
    }

    internal fun getDifficultyString(difficulty: Int?) : String{
        when(difficulty){
            0 -> return "I'M TOO YOUNG TO DIE"
            1 -> return "HURT ME PLENTY"
            2 -> return "ULTRA-VIOLENCE"
            3 -> return "NIGHTMARE"
            4 -> return "ULTRA-NIGHTMARE"
            else -> return "???"
        }
    }
}