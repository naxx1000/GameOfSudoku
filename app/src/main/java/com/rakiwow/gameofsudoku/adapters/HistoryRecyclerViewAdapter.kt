package com.rakiwow.gameofsudoku.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.data.SudokuStats
import com.rakiwow.gameofsudoku.utils.SudokuCanvasView
import java.text.DateFormat
import java.util.*

class HistoryRecyclerViewAdapter : RecyclerView.Adapter<HistoryRecyclerViewAdapter.StatsViewHolder>() {

    private var statsList = emptyList<SudokuStats>()
    lateinit var dateFormat: DateFormat

    fun HistoryRecyclerViewAdapter(locale: Locale){
        dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale)
    }

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
        val completedTime = (statsItem.completedTime?.div(60)).toString() + " Minutes, " + (statsItem.completedTime?.minus(
            statsItem.completedTime.div(60) * 60
        )).toString() + " Seconds."
        val date: Date? = statsItem.date?.let { Date(it) }
        holder.historyTextView.text = "Difficulty: ${statsItem.difficulty} Completed in: $completedTime Date: $date"
        holder.historySudokuView.grid = statsItem.grid

    }

    internal fun setStats(stats: List<SudokuStats>){
        this.statsList = stats
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return statsList.size
    }
}