package com.hmn.data.model.resp.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hmn.data.model.vos.GenreVo


class GenreListTypeConverter {
    @TypeConverter
    fun toString(genreList: List<GenreVo>?): String {
        return Gson().toJson(genreList)
    }

    @TypeConverter
    fun toGenreList(genreListJsonString: String): List<GenreVo>? {
        val genreListType = object : TypeToken<List<GenreVo>?>() {}.type
        return Gson().fromJson(genreListJsonString, genreListType)
    }
}