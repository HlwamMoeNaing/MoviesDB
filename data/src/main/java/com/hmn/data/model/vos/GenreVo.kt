package com.hmn.data.model.vos

import com.google.gson.annotations.SerializedName

data class GenreVo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)