package com.sportsapp.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T> safeApiCall(
    crossinline block: suspend () -> T
): DomainResult<T> {
    return try {
        DomainResult.Success(block())
    } catch (e: IOException) {
        DomainResult.Error(e)
    } catch (e: HttpException) {
        DomainResult.Error(e)
    } catch (e: SerializationException) {
        DomainResult.Error(e)
    } catch (t: Throwable) {
        DomainResult.Error(t)
    }
}

inline fun <T> safeApiFlow(
    crossinline block: suspend () -> T
): Flow<DomainResult<T>> = flow {
    emit(safeApiCall { block() })
}
