package com.example.proj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.proj.data.FavoritesManager
import com.example.proj.navigation.NavGraph
import com.example.proj.repository.BookRepository
import com.example.proj.ui.theme.ProjTheme
import com.example.proj.ui.viewmodel.BookDetailViewModel
import com.example.proj.ui.viewmodel.BooksViewModel
import com.example.proj.ui.viewmodel.FavoritesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val repository = BookRepository()
        val favoritesManager = FavoritesManager(this)
        
        setContent {
            ProjTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val booksViewModel: BooksViewModel = viewModel(
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return BooksViewModel(repository, favoritesManager) as T
                            }
                        }
                    )
                    
                    val bookDetailViewModel: BookDetailViewModel = viewModel(
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return BookDetailViewModel(repository, favoritesManager) as T
                            }
                        }
                    )
                    
                    val favoritesViewModel: FavoritesViewModel = viewModel(
                        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return FavoritesViewModel(repository, favoritesManager) as T
                            }
                        }
                    )
                    
                    NavGraph(
                        navController = navController,
                        booksViewModel = booksViewModel,
                        bookDetailViewModel = bookDetailViewModel,
                        favoritesViewModel = favoritesViewModel
                    )
                }
            }
        }
    }
}