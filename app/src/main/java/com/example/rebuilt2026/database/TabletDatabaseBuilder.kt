package com.example.rebuilt2026.database

import android.content.Context
import androidx.room.Room

fun tabletDatabaseBuilder(context: Context): TabletDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = TabletDatabase::class.java,
        name = "Rebuilt2026-db"
    ).build()
}


