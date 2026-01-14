package com.example.proj.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.proj.ui.viewmodel.BookDetailUiState
import com.example.proj.ui.viewmodel.BookDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    bookId: String,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    
    LaunchedEffect(bookId) {
        viewModel.loadBookDetail(bookId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły książki") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is BookDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BookDetailUiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            is BookDetailUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.bookDetail.coverUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📚", style = MaterialTheme.typography.displayLarge)
                        }
                    } else {
                        SubcomposeAsyncImage(
                            model = state.bookDetail.coverUrl,
                            contentDescription = state.bookDetail.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📚", style = MaterialTheme.typography.displayLarge)
                                }
                            }
                        )
                    }
                    
                    Text(
                        text = state.bookDetail.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Autor: ${state.bookDetail.authorNames}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    state.bookDetail.numberOfPages?.let {
                        Text(
                            text = "Liczba stron: $it",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    state.bookDetail.publishYear?.let {
                        Text(
                            text = "Rok publikacji: $it",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    Button(
                        onClick = { viewModel.toggleFavorite(state.bookDetail.key) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isFavorite) "Usuń z ulubionych" else "Dodaj do ulubionych")
                    }
                    
                    state.bookDetail.description?.let { description ->
                        Text(
                            text = "Opis:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
