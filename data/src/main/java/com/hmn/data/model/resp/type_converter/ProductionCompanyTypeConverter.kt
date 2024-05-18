package com.hmn.data.model.resp.type_converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hmn.data.model.resp.ProductionCompanies


class ProductionCompanyTypeConverter {
    @TypeConverter
    fun toString(productionCompany: List<ProductionCompanies>?): String {
        return Gson().toJson(productionCompany)
    }

    @TypeConverter
    fun toProductionCompany(productionCompanyJsonString: String): List<ProductionCompanies>? {
        val productionCompanyType = object : TypeToken<List<ProductionCompanies>?>() {}.type
        return Gson().fromJson(productionCompanyJsonString, productionCompanyType)
    }
}