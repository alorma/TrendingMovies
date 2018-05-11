package com.alorma.myapplication.ui.common

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateFormatter @Inject constructor() {
    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    infix fun format(date: Date): String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date)
}