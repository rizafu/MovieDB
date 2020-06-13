package com.rizafu.moviedb.data

enum class MoviesOverviewType {
    POPULAR, TOP_RATED, NOW_PLAYING, UPCOMING
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

enum class EmptyType {
    NO_MORE, NO_FAVORITE
}

enum class ErrorType {
    SOMETHING_WRONG, CONNECTION, UNAUTHORIZED, NOT_FOUND
}