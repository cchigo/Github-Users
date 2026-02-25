package com.chigo.githubusersapp.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

data class ErrorApiResponse(
    val type: String? = null,
    val title: String? = null,
    val status: Int? = null,
    val traceId: String? = null,
    val message: String? = null
)

sealed class BaseResponse<out T> {
    object Loading : BaseResponse<Nothing>()
    data class Success<out T>(val data: T) : BaseResponse<T>()
    data class Error(val error: ErrorApiResponse) : BaseResponse<Nothing>()
}

inline fun <T> safeApiCall(
    networkChecker: NetworkChecker,
    errorHandler: GeneralErrorHandler,
    crossinline block: suspend () -> T
): Flow<BaseResponse<T>> = flow {
    emit(BaseResponse.Loading)
    if (!networkChecker.isConnected()) {
        emit(
            BaseResponse.Error(
                ErrorApiResponse(
                    title = "No internet connection. Please check your network."
                )
            )
        )
        return@flow
    }
    try {
        emit(BaseResponse.Success(block()))
    } catch (e: Exception) {
        emit(errorHandler.getError(e))
    }
}.catch { e ->
    Timber.e(e, "Error in safeApiCall")
    emit(errorHandler.getError(e))
}