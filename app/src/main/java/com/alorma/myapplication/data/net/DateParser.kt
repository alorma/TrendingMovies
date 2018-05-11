package com.alorma.myapplication.data.net

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateParser @Inject constructor() {
    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    infix fun parse(date: String): Date = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(date)
}