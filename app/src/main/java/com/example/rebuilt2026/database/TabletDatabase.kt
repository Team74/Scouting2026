package com.example.rebuilt2026.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MatchData::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class TabletDatabase : RoomDatabase() {

    abstract fun matchDataDao(): MatchDataDao

}




