package com.sportsapp.data.events.source.remote

import com.sportsapp.core.network.api.SportsDbApi
import com.sportsapp.data.events.mapper.EventMapper.toDomainList
import com.sportsapp.data.events.model.Event
import javax.inject.Inject

class EventRemoteDataSource @Inject constructor(
    private val api: SportsDbApi
) {
}