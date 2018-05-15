package com.alorma.myapplication.data.cache

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.alorma.myapplication.data.cache.MovieEntity.Companion.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
data class MovieEntity(@PrimaryKey val id: Int,
                       val title: String,
                       val overview: String?,
                       val date: Long?,
                       val vote: Float,
                       @Embedded(prefix = "images") val images: ImagesEntity) {
    companion object {
        const val TABLE_NAME = "MOVIES"
    }
}

@Entity
class ImagesEntity(val poster: String?, val backdrop: String?)