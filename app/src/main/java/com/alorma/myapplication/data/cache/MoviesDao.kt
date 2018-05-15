package com.alorma.myapplication.data.cache

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movieList: List<MovieEntity>)

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME}")
    fun getMovies(): DataSource.Factory<Int, MovieEntity>

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE id = :id")
    fun getMovie(id: Int): MovieEntity?
}