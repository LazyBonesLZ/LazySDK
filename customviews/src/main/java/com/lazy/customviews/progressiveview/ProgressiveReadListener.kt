package com.lazy.customviews.progressiveview

interface ProgressiveReadListener {
    fun onReadCompleted()
    fun onChangePage(page:Int)
    fun onReadPause()
    fun onReadError(error:String)
}