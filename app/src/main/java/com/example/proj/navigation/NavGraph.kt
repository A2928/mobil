package com.example.proj.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proj.ui.screens.BookDetailScreen
import com.example.proj.ui.screens.FavoritesScreen
import com.example.proj.ui.screens.HomeScreen
import com.example.proj.ui.viewmodel.BookDetailViewModel
import com.example.proj.ui.viewmodel.BooksViewModel
import com.example.proj.ui.viewmodel.FavoritesViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorites : Screen("favorites")
    data class BookDetail(val bookId: String = "{bookId}") : Screen("book_detail/{bookId}") {
        fun createRoute(bookId: String) = "book_detail/$bookId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    booksViewModel: BooksViewModel,
    bookDetailViewModel: BookDetailViewModel,
    favoritesViewModel: FavoritesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = booksViewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail("").createRoute(bookId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }
        
        composable(
            route = "book_detail/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailScreen(
                viewModel = bookDetailViewModel,
                bookId = bookId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail("").createRoute(bookId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
