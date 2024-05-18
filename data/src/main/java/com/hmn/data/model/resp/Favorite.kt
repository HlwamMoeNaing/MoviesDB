package com.hmn.data.model.resp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class Favorite(
    val favorite: Boolean,
    @PrimaryKey val mediaId: Int,
    val mediaType: String,
    val image: String,
    val title: String,
    val releaseDate: String,
    val rating: Float
)