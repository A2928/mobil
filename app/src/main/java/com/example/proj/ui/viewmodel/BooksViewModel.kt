package com.example.proj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proj.data.Book
import com.example.proj.data.BookDetail
import com.example.proj.data.FavoritesManager
import com.example.proj.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BooksUiState {
    object Loading : BooksUiState()
    data class Success(val books: List<Book>) : BooksUiState()
    data class Error(val message: String) : BooksUiState()
}

class BooksViewModel(
    private val repository: BookRepository,
    private val favoritesManager: FavoritesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<BooksUiState>(BooksUiState.Loading)
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = BooksUiState.Loading
            repository.getBooks().fold(
                onSuccess = { books ->
                    _uiState.value = BooksUiState.Success(books)
                },
                onFailure = { error ->
                    _uiState.value = BooksUiState.Error(error.message ?: "Błąd pobierania danych")
                }
            )
        }
    }
    
    fun isFavorite(bookId: String): Boolean {
        return favoritesManager.isFavorite(bookId)
    }
}
