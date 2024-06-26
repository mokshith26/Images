package com.mokshith.images.navigation

sealed class Screen(val route: String) {

    data object ImagesListScreen : Screen("images_list_screen")

    data object ImageDetailsScreen : Screen("image_details_screen")

}