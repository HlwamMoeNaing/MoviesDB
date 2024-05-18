package com.hmn.data.model.resp

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.hmn.data.model.vos.ActorVo

class GetCreditByMovieResponse(
    @SerializedName("cast")
    @ColumnInfo(name = "cast")
    val casts: List<ActorVo>?,

    @SerializedName("crew")
    @ColumnInfo(name = "crew")
    val crew: List<ActorVo>?
)