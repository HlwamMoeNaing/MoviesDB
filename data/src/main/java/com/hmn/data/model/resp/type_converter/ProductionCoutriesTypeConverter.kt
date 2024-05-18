package com.hmn.data.model.resp.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hmn.data.model.resp.ProductionCountries


class ProductionCoutriesTypeConverter {
    @TypeConverter
    fun toString(productionCountries: List<ProductionCountries>?): String {
        return Gson().toJson(productionCountries)
    }

    @TypeConverter
    fun toProductionCountry(productionCountryJsonString: String): List<ProductionCountries>? {
        val productionCountryType = object : TypeToken<List<ProductionCountries>?>() {}.type
        return Gson().fromJson(productionCountryJsonString, productionCountryType)
    }
}