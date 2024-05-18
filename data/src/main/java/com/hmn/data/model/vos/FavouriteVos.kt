package com.hmn.data.model.vos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favourite_table")
data class FavouriteVos(
    @PrimaryKey
    val id:Int = 0,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String ?,

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,


    @SerializedName("video")
    @ColumnInfo(name = "video")
    val video: Boolean,

    @SerializedName("genres")
    @ColumnInfo(name = "genres")
    val genres: List<GenreVo>?,

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,
){
    fun getRatingBaseOnFiveStar(): Float {
        return voteAverage?.div(2)?.toFloat() ?: 0.0f
    }
}