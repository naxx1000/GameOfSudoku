package com.rakiwow.gameofsudoku.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// https://medium.com/@abhilashmyworld/communicate-between-fragments-using-viewmodel-e83344e9df53?

//Holds LiveData of the app color scheme
class MainSharedViewModel : ViewModel(){
    val colors = MutableLiveData<IntArray>()

    fun setColorsArray(colors: IntArray){
        this.colors.value = colors
    }
}