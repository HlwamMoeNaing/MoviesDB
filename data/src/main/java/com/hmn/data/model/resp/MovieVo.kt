package com.hmn.data.model.resp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.hmn.data.model.resp.type_converter.BelongsToCollectionTypeConverter
import com.hmn.data.model.resp.type_converter.GenreIdTypeConverter
import com.hmn.data.model.resp.type_converter.GenreListTypeConverter
import com.hmn.data.model.resp.type_converter.ProductionCompanyTypeConverter
import com.hmn.data.model.resp.type_converter.ProductionCoutriesTypeConverter
import com.hmn.data.model.resp.type_converter.SpokenLauguageTypeConverter
import com.hmn.data.model.vos.GenreVo


@Entity(tableName = "movies")
@TypeConverters(
    GenreListTypeConverter::class,
    ProductionCompanyTypeConverter::class,
    ProductionCoutriesTypeConverter::class,
    SpokenLauguageTypeConverter::class,
    GenreIdTypeConverter::class,
    BelongsToCollectionTypeConverter::class

)
data class MovieVo(
    @SerializedName("adult")
    @ColumnInfo(name = "adult")
    val adult: Boolean = false,

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String?,

    @SerializedName("genre_ids")
    @ColumnInfo(name = "genre_ids")
    val genreIds: List<Int>?,

    @SerializedName("id")
    @PrimaryKey
    val id: Int = 0,

    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    val originalLanguage: String ?,

    @SerializedName("original_title")
    @ColumnInfo(name = "original_title")
    val originalTitle: String ?,

    @SerializedName("overview")
    @ColumnInfo(name = "overview")
    val overview: String ?,

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Double?,

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,

    @SerializedName("video")
    @ColumnInfo(name = "video")
    val video: Boolean,

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,

    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    val voteCount: Int?,

    @SerializedName("production_companies")
    @ColumnInfo(name = "production_companies")
    val productionCompanies: List<ProductionCompanies>?,

    @SerializedName("production_countries")
    @ColumnInfo(name = "production_countries")
    val productionCountries: List<ProductionCountries>?,

    @SerializedName("spoken_languages")
    @ColumnInfo(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguages>?,

    @SerializedName("status")
    @ColumnInfo(name = "status")
    val status: String ?,

    @SerializedName("tagline")
    @ColumnInfo(name = "tagline")
    val tagLine: String?,

    @SerializedName("genres")
    @ColumnInfo(name = "genres")
    val genres: List<GenreVo>?,

    @SerializedName("belongs_to_collection")
    @ColumnInfo(name = "belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,

    @SerializedName("budget")
    @ColumnInfo(name = "budget")
    val budget: Int?,

    @SerializedName("homepage")
    @ColumnInfo(name = "homepage")
    val homepage: String?,

    @SerializedName("imdb_id")
    @ColumnInfo(name = "imdb_id")
    val imdbId: String?,

    @SerializedName("runtime")
    @ColumnInfo(name = "runtime")
    val runtime: Int?,

    @ColumnInfo(name = "type")
    var type: String?,

    var isFavourite:Boolean = false,
    var category:String = ""


) {
    fun getRatingBaseOnFiveStar(): Float {
        return voteAverage?.div(2)?.toFloat() ?: 0.0f
    }

    fun getGenresAsCommaSeparatedString(): String {
        return genres?.joinToString { it.name } ?: ""
    }

    fun getProductionCountryAsCommaSeparatedString(): String {
        // return productionCountries?.joinToString { it.name } ?:""
        return productionCountries?.joinToString { it -> it.name } ?: ""
    }


}

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String ="",

    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String ="",

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String = ""
)


data class ProductionCompanies(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,

    @SerializedName("logo_path")
    @ColumnInfo(name = "logo_path")
    val LogoPath: String?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?,

    @SerializedName("origin_country")
    @ColumnInfo(name = "origin_country")
    val originCountry: String?
)

data class ProductionCountries(
    @SerializedName("iso_3166_1")
    @ColumnInfo(name = "iso_3166_1")
    val iso: String?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String
)

data class SpokenLanguages(
    @SerializedName("english_name")
    @ColumnInfo(name = "english_name")
    val englishName: String?,

    @SerializedName("iso_639_1")
    @ColumnInfo(name = "iso_639_1")
    val iso: String?,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    val name: String?


)