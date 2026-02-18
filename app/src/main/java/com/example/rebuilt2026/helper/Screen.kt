package com.example.rebuilt2026.helper

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object PreMatch : Screen()

    @Serializable
    data object Auton : Screen()

    @Serializable
    data object Teleop : Screen()

    @Serializable
    data object PostMatch : Screen()

    @Serializable
    data object Admin : Screen()

}