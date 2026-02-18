package com.sportsapp.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamsResponse(
    @SerialName("teams")
    val teams: List<TeamDto>? = null
)

@Serializable
data class TeamDto(
    @SerialName("idTeam")
    val id: String? = null,
    @SerialName("strTeam")
    val name: String? = null,
    @SerialName("strTeamShort")
    val shortName: String? = null,
    @SerialName("strSport")
    val sport: String? = null,
    @SerialName("strLeague")
    val league: String? = null,
    @SerialName("strCountry")
    val country: String? = null,
    @SerialName("strStadium")
    val stadium: String? = null,
    @SerialName("strStadiumLocation")
    val stadiumLocation: String? = null,
    @SerialName("intStadiumCapacity")
    val stadiumCapacity: String? = null,
    @SerialName("strBadge")
    val badge: String? = null,
    @SerialName("strTeamJersey")
    val jersey: String? = null,
    @SerialName("strDescriptionEN")
    val description: String? = null,
    @SerialName("intFormedYear")
    val formedYear: String? = null,
    @SerialName("strWebsite")
    val website: String? = null,
    @SerialName("strFacebook")
    val facebook: String? = null,
    @SerialName("strTwitter")
    val twitter: String? = null,
    @SerialName("strInstagram")
    val instagram: String? = null,
    @SerialName("strYoutube")
    val youtube: String? = null
)
