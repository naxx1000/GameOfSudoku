package com.rakiwow.gameofsudoku.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.adapters.HistoryRecyclerViewAdapter
import com.rakiwow.gameofsudoku.viewmodel.StatsViewModel
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragments : Fragment() {

    private lateinit var adapter: HistoryRecyclerViewAdapter
    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryRecyclerViewAdapter(resources.configuration.locale)
        recycler_history.adapter = adapter
        recycler_history.layoutManager = LinearLayoutManager(activity)

        statsViewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        statsViewModel.statsData.observe(viewLifecycleOwner, Observer{ stats ->
            stats?.let{adapter.setStats(it)}
        })
    }
}
