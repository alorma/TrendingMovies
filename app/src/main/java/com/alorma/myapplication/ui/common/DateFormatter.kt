package com.alorma.myapplication.ui.common

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateFormatter @Inject constructor() {
    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val YEAR_FORMAT = "yyyy"
    }

    infix fun format(date: Date): String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date)

    infix fun formatYear(date: Date): String = SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(date)
}