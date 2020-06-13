package com.rizafu.moviedb.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.LoadRequestBuilder
import coil.request.RequestDisposable
import com.rizafu.moviedb.data.ErrorType
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun RecyclerView.setupLinearVertical(
    context: Context,
    adapter: RecyclerViewListAdapter,
    onItemClick: (view: View, item: ItemModel) -> Unit
) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    this.adapter = adapter.apply {
        setOnItemClick(onItemClick)
    }
}

fun ImageView.loadImageTMDB(
    imagePath: String,
    builder: LoadRequestBuilder.() -> Unit = {}
): RequestDisposable {
    return load("https://image.tmdb.org/t/p/w500/${imagePath}") {
        crossfade(true)
        builder.invoke(this)
    }
}

val Int.dp: Int // or just return Float here
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val String.formattedDate: String
    get() = LocalDate.parse(
        this,
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    ).format(
        DateTimeFormatter.ofPattern("MMM dd, YYYY")
    )

fun Throwable.errorMapper(): ErrorType {
    return when (this) {
        is UnknownHostException,
        is SocketTimeoutException -> ErrorType.CONNECTION
        is HttpException -> {
            when {
                this.code() == HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    ErrorType.UNAUTHORIZED
                }
                this.code() == HttpURLConnection.HTTP_NOT_FOUND -> {
                    ErrorType.NOT_FOUND
                }
                else -> {
                    ErrorType.SOMETHING_WRONG
                }
            }
        }
        else -> {
            ErrorType.SOMETHING_WRONG
        }
    }
}