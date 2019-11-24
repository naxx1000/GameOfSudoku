package com.rakiwow.gameofsudoku.utils

import java.util.*

open class StopWatchTask : TimerTask() {

    var mIsTicking = false

    override fun run() {
        mIsTicking = true
    }

    fun isTicking() : Boolean{
        return mIsTicking
    }
}