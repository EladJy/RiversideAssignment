package com.ej.riversideassignment.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ej.riversideassignment.model.TitleDetails

@Database(entities = [TitleDetails::class], version = 1)
abstract class TitlesDb : RoomDatabase() {
    abstract fun titleDetailsDao(): TitleDetailsDao
}