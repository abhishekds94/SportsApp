package com.sportsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sportsapp.feature.leagues.LeaguesScreen
import com.sportsapp.feature.search.SearchScreen
import com.sportsapp.feature.teamdetail.TeamDetailScreen

@Composable
fun SportsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationDestinations.Leagues.route,
        modifier = modifier
    ) {

        composable(NavigationDestinations.Leagues.route) {
            LeaguesScreen(
                onTeamClick = { teamId, teamName ->
                    navController.navigate(
                        NavigationDestinations.TeamDetail.createRoute(teamId, teamName)
                    )
                }
            )
        }

        composable(NavigationDestinations.Search.route) {
            SearchScreen(
                onTeamClick = { teamId, teamName ->
                    navController.navigate(
                        NavigationDestinations.TeamDetail.createRoute(teamId, teamName)
                    )
                }
            )
        }

        composable(
            route = NavigationDestinations.TeamDetail.route,
            arguments = listOf(
                navArgument("teamId") { type = NavType.StringType },
                navArgument("teamName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            val teamName = backStackEntry.arguments?.getString("teamName") ?: ""

            key(teamName) {
                TeamDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

    }
}
