package com.hmn.data.model.resp.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hmn.data.model.resp.SpokenLanguages


class SpokenLauguageTypeConverter {
    @TypeConverter
    fun toString(spokenLanguages: List<SpokenLanguages>?): String {
        return Gson().toJson(spokenLanguages)
    }

    @TypeConverter
    fun toSpokenLanguages(spokenLaugugeJsonString: String): List<SpokenLanguages>? {
        val spokenLanguagesType = object : TypeToken<List<SpokenLanguages>?>() {}.type
        return Gson().fromJson(spokenLaugugeJsonString, spokenLanguagesType)
    }
}