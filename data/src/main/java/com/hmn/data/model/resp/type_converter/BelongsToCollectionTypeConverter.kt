package com.hmn.data.model.resp.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hmn.data.model.resp.BelongsToCollection

class BelongsToCollectionTypeConverter {
    @TypeConverter
    fun toString(belongToCollection: BelongsToCollection?): String {
        return Gson().toJson(belongToCollection)
    }

    @TypeConverter
    fun toBelongToCollection(belongToCollectionJsonString: String): BelongsToCollection? {
        val belongsToCollectionType = object : TypeToken<BelongsToCollection?>() {}.type
        return Gson().fromJson(belongToCollectionJsonString, belongsToCollectionType)
    }
}