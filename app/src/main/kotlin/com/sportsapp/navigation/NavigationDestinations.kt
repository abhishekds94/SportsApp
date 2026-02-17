package com.sportsapp.navigation

sealed class NavigationDestinations(val route: String) {
    data object Leagues : NavigationDestinations("leagues")
    data object Search : NavigationDestinations("search")
    data object TeamDetail : NavigationDestinations("team_detail/{teamId}/{teamName}") {
        fun createRoute(teamId: String, teamName: String): String {
            return "team_detail/$teamId/$teamName"
        }
    }
}
