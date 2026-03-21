package com.example.rebuilt2026.helper

import kotlinx.serialization.Serializable

@Serializable
enum class Climb(val label: String) {

    NO_CLIMB("No Climb"),
    LEVEL_1("Level 1"),
    LEVEL_2("Level 2"),
    LEVEL_3("Level 3");

    companion object {

        /**
         * Converts the given number to the matching enum ordinal.
         */
        fun fromOrdinal(ordinal: Int) = entries.first { it.ordinal == ordinal }

    }
}