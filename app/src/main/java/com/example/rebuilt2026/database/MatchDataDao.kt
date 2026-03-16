package com.example.rebuilt2026.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MatchDataDao {

    @Query("SELECT * FROM MatchData")
    suspend fun all(): List<MatchData>

    @Query("SELECT * FROM MatchData WHERE id = :id")
    suspend fun fetch(id: Int): MatchData

    @Insert
    suspend fun update(data: MatchData)

    @Delete
    suspend fun delete(data: MatchData)

    @Query("DELETE FROM MatchData")
    suspend fun nuke()

}