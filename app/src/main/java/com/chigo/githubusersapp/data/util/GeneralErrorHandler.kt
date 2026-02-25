package com.chigo.githubusersapp.data.util

import java.io.InterruptedIOException
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralErrorHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getError(throwable: Throwable): BaseResponse.Error {
        val errorResponse = when (throwable) {
            is UnknownHostException -> ErrorApiResponse(
                title = "No internet connection. Please check your network."
            )
            is SocketException -> ErrorApiResponse(
                title = "Network error. Please try again."
            )
            is InterruptedIOException -> ErrorApiResponse(
                title = "Request timed out. Please try again."
            )
            is HttpException -> {
                val parsed = convertErrorBody<ErrorApiResponse>(throwable)
                ErrorApiResponse(
                    title = parsed?.title ?: when (throwable.code()) {
                        403 -> "GitHub API rate limit exceeded. Please try again later."
                        404 -> "User not found."
                        500 -> "GitHub server error. Please try again later."
                        else -> "Something went wrong. Please try again."
                    },
                    status = parsed?.status ?: throwable.code(),
                    type = parsed?.type,
                    traceId = parsed?.traceId
                )
            }
            else -> ErrorApiResponse(
                title = "An unexpected error occurred: ${throwable.message.orEmpty()}"
            )
        }
        return BaseResponse.Error(errorResponse)
    }
}

inline fun <reified T> convertErrorBody(t: HttpException): T? {
    return try {
        t.response()?.errorBody()?.let {
            val type = object : TypeToken<T>() {}.type
            Gson().fromJson(it.charStream(), type)
        }
    } catch (e: IOException) {
        Timber.e(e, "Failed to read error body")
        null
    } catch (e: JsonSyntaxException) {
        Timber.e(e, "Failed to parse error body")
        null
    }
}