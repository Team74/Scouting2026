package com.example.rebuilt2026.helper

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable

@Serializable
enum class Scouter(val pos: String) {

    NONE("Device In Error"),
    BLUE_1("Blue 1"),
    BLUE_2("Blue 2"),
    BLUE_3("Blue 3"),
    RED_1("Red 1"),
    RED_2("Red 2"),
    RED_3("Red 3");

    /**
     * Returns the color associated with the enum value.
     */
    fun color(): Color {
        return when (this) {
            BLUE_1, BLUE_2, BLUE_3  -> Color.Blue
            RED_1, RED_2, RED_3     -> Color.Red
            NONE                    -> Color.Magenta
        }
    }

    companion object {

        /**
         * Converts the given string to a scouter enum value.
         */
        fun fromString(scouter: String): Scouter {
            return entries.firstOrNull {
                it.name == scouter
            } ?: NONE
        }

    }

}