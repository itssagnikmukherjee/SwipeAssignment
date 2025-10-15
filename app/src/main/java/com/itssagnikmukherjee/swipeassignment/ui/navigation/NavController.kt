package com.itssagnikmukherjee.swipeassignment.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itssagnikmukherjee.swipeassignment.ui.screens.AddProductScreen
import com.itssagnikmukherjee.swipeassignment.ui.screens.ProductListScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductListScreen,
        enterTransition = { slideInHorizontally(initialOffsetX = {1000})},
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
    ){
        composable<AddProductScreen> { AddProductScreen(navController = navController, modifier = modifier) }
        composable<ProductListScreen>{ ProductListScreen(navController = navController, modifier = modifier) }
    }
}