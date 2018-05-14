package com.alorma.myapplication.data.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MoviesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildMoviesDatabase(context).also { INSTANCE = it }
        }

        private fun buildMoviesDatabase(context: Context) = Room.databaseBuilder(context,
                AppDatabase::class.java, "movies.db").build()
    }
}
