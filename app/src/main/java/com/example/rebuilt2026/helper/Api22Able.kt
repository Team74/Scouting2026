package com.example.rebuilt2026.helper

import java.util.Calendar
import kotlin.time.Instant

object Api22Able {

    fun getTimestamp(): String {

        // API 22 compatible date formatter
        val time = Calendar.getInstance()
        val year = time.get(Calendar.YEAR)
        val month = time.get(Calendar.MONTH + 1)
        val day = time.get(Calendar.DAY_OF_MONTH)

        return "$year-$month-$day"

    }

}