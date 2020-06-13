package com.rizafu.moviedb.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class MovieModel(
    val id: Int = -1,
    val adult: Boolean = true,
    val budget: Int = 0,
    val genres: List<GenreModel> = listOf(),
    val homepage: String? = null,
    val overview: String? = null,
    val popularity: Double = 0.0,
    val revenue: Int = 0,
    val runtime: Int? = null,
    val status: String = "",
    val tagline: String? = null,
    val title: String = "",
    val video: Boolean = false,
    @Json(name = "backdrop_path") val backdropPath: String? = null,
    @Json(name = "imdb_id") val imdbId: String? = null,
    @Json(name = "original_language") val originalLanguage: String = "",
    @Json(name = "original_title") val originalTitle: String = "",
    @Json(name = "poster_path") val posterPath: String? = null,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompanyModel> = listOf(),
    @Json(name = "production_countries") val productionCountries: List<ProductionCountryModel> = listOf(),
    @Json(name = "release_date") val releaseDate: String = "",
    @Json(name = "spoken_languages") val spokenLanguages: List<SpokenLanguageModel> = listOf(),
    @Json(name = "vote_average") val voteAverage: Double = 0.0,
    @Json(name = "vote_count") val voteCount: Int = 0
)

@JsonSerializable
data class GenreModel(
    val id: Int = -1,
    val name: String = ""
)

@JsonSerializable
data class ProductionCompanyModel(
    val id: Int = -1,
    val name: String = "",
    @Json(name = "logo_path") val logoPath: String? = null,
    @Json(name = "origin_country") val originCountry: String = ""
)

@JsonSerializable
data class ProductionCountryModel(
    val iso_3166_1: String = "",
    val name: String = ""
)

@JsonSerializable
data class SpokenLanguageModel(
    val iso_639_1: String = "",
    val name: String = ""
)