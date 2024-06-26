package com.mokshith.images.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mokshith.images.presentation.imagesList.ImagesListScreen
import com.mokshith.images.presentation.imageDetails.ImageDetailsScreen

@Composable
fun ScreenNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ImagesListScreen.route
    ) {
        composable(route = Screen.ImagesListScreen.route) {
            ImagesListScreen(navController)
        }

        composable(route = Screen.ImageDetailsScreen.route+"/{title}/{description}/{author}/{publishedDate}/{imageUrl}") {
            val title = it.arguments?.getString("title") ?: ""
            val description = it.arguments?.getString("description") ?: ""
            val author = it.arguments?.getString("author") ?: ""
            val publishedDate = it.arguments?.getString("publishedDate") ?: ""
            val imageUrl = it.arguments?.getString("imageUrl") ?: ""
            ImageDetailsScreen(navController,title,description,author,publishedDate,imageUrl)
        }
    }
}