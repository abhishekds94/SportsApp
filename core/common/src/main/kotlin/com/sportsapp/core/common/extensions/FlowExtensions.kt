package com.sportsapp.core.common.extensions

import com.sportsapp.core.common.util.Constants
import com.sportsapp.core.common.util.NoConnectivityException
import com.sportsapp.core.common.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Extension function to handle errors at the Flow level
 * Converts exceptions into Resource.Error states
 */
fun <T> Flow<Resource<T>>.asResult(): Flow<Resource<T>> = this
    .catch { exception ->
        val errorMessage = when (exception) {
            is NoConnectivityException -> Constants.ErrorMessages.NETWORK_ERROR
            else -> exception.message ?: Constants.ErrorMessages.SERVER_ERROR
        }
        emit(Resource.Error(errorMessage, exception))
    }