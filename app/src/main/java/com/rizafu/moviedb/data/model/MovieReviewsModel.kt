package com.rizafu.moviedb.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class MovieReviewModel(
    val author: String = "",
    val content: String = "",
    val id: String = "",
    val url: String = ""
)