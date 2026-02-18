package com.example.rebuilt2026.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class MatchData(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /* TODO: Put entries here */

)
