package com.example.rebuilt2026.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MatchData::class], version = 1)
abstract class TabletDatabase : RoomDatabase() {

    abstract fun matchDataDao(): MatchDataDao

}




