package com.rizafu.moviedb

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rizafu.moviedb.data.ErrorType
import com.rizafu.moviedb.utils.errorMapper
import com.rizafu.moviedb.utils.formattedDate
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.assertEquals

object ExtensionTest : Spek({

    describe("String date format") {
        val dateString by memoized { "2020-12-21".formattedDate }
        it("should return") {
            assertEquals(expected = "Dec 21, 2020", actual = dateString)
        }
    }

    describe("Error type") {
        it("should return error connection") {
            assertEquals(
                expected = ErrorType.CONNECTION,
                actual = UnknownHostException().errorMapper()
            )
        }

        it("should return error connection") {
            assertEquals(
                expected = ErrorType.CONNECTION,
                actual = SocketTimeoutException().errorMapper()
            )
        }

        it("should return error something wrong") {
            assertEquals(expected = ErrorType.SOMETHING_WRONG, actual = Throwable().errorMapper())
        }

        val error: HttpException = mock()
        it("should return error unauthorized") {
            whenever(error.code()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED)
            assertEquals(expected = ErrorType.UNAUTHORIZED, actual = error.errorMapper())
        }

        it("should return error not found") {
            whenever(error.code()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND)
            assertEquals(expected = ErrorType.NOT_FOUND, actual = error.errorMapper())
        }

        it("should return error something wrong") {
            whenever(error.code()).thenReturn(HttpURLConnection.HTTP_BAD_GATEWAY)
            assertEquals(expected = ErrorType.SOMETHING_WRONG, actual = error.errorMapper())
        }
    }
})