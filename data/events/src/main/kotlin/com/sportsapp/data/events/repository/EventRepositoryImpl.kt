package com.sportsapp.data.events.repository

import com.sportsapp.core.common.util.Resource
import com.sportsapp.data.events.model.Event
import com.sportsapp.data.events.source.remote.EventRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val remoteDataSource: EventRemoteDataSource
) : EventRepository {

}