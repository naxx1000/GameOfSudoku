package com.rakiwow.gameofsudoku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.rakiwow.gameofsudoku.R
import com.rakiwow.gameofsudoku.viewmodel.MainSharedViewModel
import java.lang.Exception

private const val FRAGMENT_ID = 4

class AboutFragment : Fragment() {

    private lateinit var sharedViewModel: MainSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = activity?.run{
            ViewModelProvider(this).get(MainSharedViewModel::class.java)
        }?: throw Exception("Invalid Activity")
        sharedViewModel.currentFragment = FRAGMENT_ID
    }

}