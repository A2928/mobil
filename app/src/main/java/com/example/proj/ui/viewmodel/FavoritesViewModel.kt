package com.example.proj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proj.data.Book
import com.example.proj.data.FavoritesManager
import com.example.proj.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val books: List<Book>) : FavoritesUiState()
    data class Empty(val message: String) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
}

class FavoritesViewModel(
    private val repository: BookRepository,
    private val favoritesManager: FavoritesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = FavoritesUiState.Loading
            val favoriteIds = favoritesManager.getFavorites()
            
            if (favoriteIds.isEmpty()) {
                _uiState.value = FavoritesUiState.Empty("Brak ulubionych książek")
                return@launch
            }
            
            val books = mutableListOf<Book>()
            favoriteIds.forEach { id ->
                repository.getBookDetail(id).fold(
                    onSuccess = { bookDetail ->
                        books.add(
                            Book(
                                key = bookDetail.key,
                                title = bookDetail.title,
                                authorNames = bookDetail.authors.map { it.name },
                                coverId = bookDetail.coverId
                            )
                        )
                    },
                    onFailure = { }
                )
            }
            
            if (books.isEmpty()) {
                _uiState.value = FavoritesUiState.Empty("Brak ulubionych książek")
            } else {
                _uiState.value = FavoritesUiState.Success(books)
            }
        }
    }
}
