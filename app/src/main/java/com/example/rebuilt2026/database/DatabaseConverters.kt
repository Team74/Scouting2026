package com.example.rebuilt2026.database

import androidx.room.TypeConverter
import com.example.rebuilt2026.helper.Climb

class DatabaseConverters {

    @TypeConverter
    fun ord2climb(ordinal: Int) = Climb.fromOrdinal(ordinal)
    @TypeConverter
    fun climb2ord(climb: Climb) = climb.ordinal

}