package com.sportsapp.core.common.util

object Constants {
    object Api {
        const val BASE_URL = "https://www.thesportsdb.com/"
        const val TIMEOUT_SECONDS = 30L
    }

    object Pagination {
        const val INITIAL_PAGE_SIZE = 10
        const val LOAD_MORE_SIZE = 4
    }

    object ErrorMessages {
        const val NETWORK_ERROR = "No internet connection. Please check your network."
        const val SERVER_ERROR = "Something went wrong. Please try again."
        const val SEARCH_ERROR = "Failed to search. Please try again."
        const val EMPTY_SEARCH = "Please enter at least 3 characters"
        const val NO_RESULTS = "No results found"
        const val NO_TEAMS = "No teams found for this league"
    }

    object DateFormats {
        const val API_DATE_FORMAT = "yyyy-MM-dd"
        const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"
        const val DISPLAY_TIME_FORMAT = "HH:mm"
    }

    object Sports {
        const val SOCCER = "Soccer"
        const val BASKETBALL = "Basketball"
        const val BASEBALL = "Baseball"
        const val CRICKET = "Cricket"

        val ALL_SPORTS = listOf(SOCCER, BASKETBALL, BASEBALL, CRICKET)

        val SPORT_LEAGUES = mapOf(
            SOCCER to listOf(
                "English Premier League",
                "Spanish La Liga",
                "German Bundesliga",
                "Italian Serie A",
                "French Ligue 1"
            ),
            BASKETBALL to listOf(
                "NBA",
                "FIBA Basketball World Cup"
            ),
            BASEBALL to listOf(
                "MLB",
                "World Baseball Classic"
            ),
            CRICKET to listOf(
                "Indian Premier League",
                "Big Bash League",
                "ICC Cricket World Cup"
            )
        )
    }
}