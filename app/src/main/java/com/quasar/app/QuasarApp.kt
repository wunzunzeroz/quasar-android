package com.quasar.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quasar.app.auth.ui.LandingScreen
import com.quasar.app.channels.ui.ChannelDetailScreen
import com.quasar.app.channels.ui.ChannelsScreen
import com.quasar.app.map.ui.CirclesScreen
import com.quasar.app.map.ui.MapScreen
import com.quasar.app.map.ui.PolyLinesScreen
import com.quasar.app.map.ui.PolygonsScreen
import com.quasar.app.map.ui.WaypointsScreen

enum class QuasarScreen {
    LandingScreen,
    MapScreen,
    WaypointsScreen,
    PolylinesScreen,
    PolygonsScreen,
    CirclesScreen,
    ChannelsScreen,
    ChannelDetailScreen
}

@Composable
fun QuasarApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = QuasarScreen.LandingScreen.name,
    ) {
        composable(route = QuasarScreen.LandingScreen.name) { LandingScreen(navController) }

        composable(route = QuasarScreen.MapScreen.name) { MapScreen(navController) }
        composable(route = QuasarScreen.WaypointsScreen.name) { WaypointsScreen(navController) }
        composable(route = QuasarScreen.PolylinesScreen.name) { PolyLinesScreen(navController) }
        composable(route = QuasarScreen.PolygonsScreen.name) { PolygonsScreen(navController) }
        composable(route = QuasarScreen.CirclesScreen.name) { CirclesScreen(navController) }

        composable(route = QuasarScreen.ChannelsScreen.name) { ChannelsScreen(navController) }
        composable(
            route = "${QuasarScreen.ChannelDetailScreen.name}/{channelId}",
            arguments = listOf(navArgument("channelId") { type = NavType.StringType })
        ) { backstackEntry ->
            ChannelDetailScreen(navController,
                backstackEntry.arguments?.getString("channelId")
                    ?: throw Exception("Unable to get Channel ID from navigation arguments")
            )
        }
    }
}


