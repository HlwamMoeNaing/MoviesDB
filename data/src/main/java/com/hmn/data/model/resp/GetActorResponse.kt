package com.hmn.data.model.resp

import com.google.gson.annotations.SerializedName
import com.hmn.data.model.vos.ActorVo

data class GetActorResponse(
    @SerializedName("results")
    val results:List<ActorVo>?
)