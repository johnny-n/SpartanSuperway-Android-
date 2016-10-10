package com.engr195.spartansuperway.spartansuperway.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(text: CharSequence, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}
