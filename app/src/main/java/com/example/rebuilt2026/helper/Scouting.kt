package com.example.rebuilt2026.helper

import kotlinx.serialization.Serializable

@Serializable
enum class Scouting(pos: String) {

    ERROR_STATE("Device In Error"),
    BLUE_1("Blue 1"),
    BLUE_2("Blue 2"),
    BLUE_3("Blue 3"),
    RED_1("Red 1"),
    RED_2("Red 2"),
    RED_3("Red 3")

}