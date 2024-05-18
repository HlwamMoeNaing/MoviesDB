package com.hmn.data.model.resp

import com.google.gson.annotations.SerializedName
import com.hmn.data.model.vos.DateVO

data class MovieListResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("dates")
    val dates: DateVO?,
    @SerializedName("results")
    val results: List<MovieVo>?
)