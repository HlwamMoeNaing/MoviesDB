package com.hmn.data.model.resp

import com.google.gson.annotations.SerializedName

data class Video(
    val id: Int,
    val results: List<Result>
)

data class Result(
    @SerializedName("id")
    val id: String,

    @SerializedName("iso_3166_1")
    val isoOne: String,

    @SerializedName("iso_639_1")
    val isoTwo: String,

    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("official")
    val official: Boolean,

    @SerializedName("published_at")
    val publishedAt: String,

    @SerializedName("site")
    val site: String,

    @SerializedName("size")
    val size: Int,

    @SerializedName("type")
    val type: String
)