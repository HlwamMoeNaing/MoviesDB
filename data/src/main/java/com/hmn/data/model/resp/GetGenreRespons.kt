package com.hmn.data.model.resp

import com.google.gson.annotations.SerializedName
import com.hmn.data.model.vos.GenreVo

data class GetGenreRespons(
    @SerializedName("genres")
    val genre: List<GenreVo>
)