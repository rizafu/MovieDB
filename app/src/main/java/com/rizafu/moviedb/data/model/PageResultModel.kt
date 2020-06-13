package com.rizafu.moviedb.data.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class PageResultModel<T>(
    val page: Int = 0,
    val results: List<T> = listOf(),
    val dates: DateModel? = null,
    @Json(name = "total_pages") val totalPages: Int = 0,
    @Json(name = "total_results") val totalResults: Int = 0
)

@JsonSerializable
data class DateModel(
    val maximum: String = "",
    val minimum: String = ""
)