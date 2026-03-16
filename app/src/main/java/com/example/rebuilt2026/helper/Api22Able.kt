package com.example.rebuilt2026.helper

import java.util.Calendar

/**
 * Singleton containing definitions for all the helper functions requiring a pre-api22
 * implementation.
 */
object Api22Able {

    /**
     * Fetches the current date timestamp and returns it as a string.
     */
    fun getTimestamp(): String {

        // API 22 compatible date formatter
        val time = Calendar.getInstance()
        val year = time.get(Calendar.YEAR)
        val month = time.get(Calendar.MONTH + 1)
        val day = time.get(Calendar.DAY_OF_MONTH)

        return "$year-$month-$day"

    }

}