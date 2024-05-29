package com.quasar.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quasar.app.auth.ui.LandingScreen
import com.quasar.app.map.ui.MapScreen

enum class QuasarScreen {
    LandingScreen, MapScreen
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
    }
}


