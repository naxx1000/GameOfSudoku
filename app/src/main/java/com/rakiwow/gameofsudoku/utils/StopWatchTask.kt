package com.rakiwow.gameofsudoku.utils

import java.util.*

class StopWatchTask : TimerTask() {

    var mIsTicking = false

    override fun run() {
        mIsTicking = true
    }

    fun isTicking() : Boolean{
        return mIsTicking
    }
}