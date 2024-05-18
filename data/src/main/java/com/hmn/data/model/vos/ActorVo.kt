package com.hmn.data.model.vos

import com.google.gson.annotations.SerializedName
import com.hmn.data.model.resp.MovieVo

data class ActorVo(
    @SerializedName("adult")
    val adult: Boolean?,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("known_for")
    val knownFor: List<MovieVo>?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Double?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("original_name")
    val originalName: String?,
    @SerializedName("cast_id")
    val castId:Int?,
    @SerializedName("character")
    val character:String?,
    @SerializedName("credit_id")
    val creditId:String?,
    @SerializedName("order")
    val order:Int?,
    @SerializedName("department")
    val department:String?,
    @SerializedName("job")
    val job:String?

)
