package com.alorma.data.net

import java.text.SimpleDateFormat
import java.util.*

class DateParser{
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    infix fun parse(date: String): Date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(date)
}