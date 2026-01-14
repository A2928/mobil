package com.example.proj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proj.data.BookDetail
import com.example.proj.data.FavoritesManager
import com.example.proj.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BookDetailUiState {
    object Loading : BookDetailUiState()
    data class Success(val bookDetail: BookDetail) : BookDetailUiState()
    data class Error(val message: String) : BookDetailUiState()
}

class BookDetailViewModel(
    private val repository: BookRepository,
    private val favoritesManager: FavoritesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<BookDetailUiState>(BookDetailUiState.Loading)
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    fun loadBookDetail(workId: String) {
        viewModelScope.launch {
            _uiState.value = BookDetailUiState.Loading
            _isFavorite.value = favoritesManager.isFavorite(workId)
            
            repository.getBookDetail(workId).fold(
                onSuccess = { bookDetail ->
                    _uiState.value = BookDetailUiState.Success(bookDetail)
                },
                onFailure = { error ->
                    _uiState.value = BookDetailUiState.Error(error.message ?: "Błąd pobierania danych")
                }
            )
        }
    }
    
    fun toggleFavorite(bookId: String) {
        if (_isFavorite.value) {
            favoritesManager.removeFavorite(bookId)
        } else {
            favoritesManager.addFavorite(bookId)
        }
        _isFavorite.value = !_isFavorite.value
    }
}
