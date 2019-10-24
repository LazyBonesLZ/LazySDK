package com

import android.content.Context
import android.widget.Toast

object Utils {
    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}