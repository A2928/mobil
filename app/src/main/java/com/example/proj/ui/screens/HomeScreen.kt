package com.example.proj.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.proj.data.Book
import com.example.proj.ui.viewmodel.BooksUiState
import com.example.proj.ui.viewmodel.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: BooksViewModel,
    onBookClick: (String) -> Unit,
    onFavoritesClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista książek") },
                actions = {
                    TextButton(onClick = onFavoritesClick) {
                        Text("Ulubione")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is BooksUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is BooksUiState.Error -> {
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadBooks() }) {
                        Text("Spróbuj ponownie")
                    }
                }
            }
            is BooksUiState.Success -> {
                if (state.books.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Brak książek")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.books) { book ->
                            BookItem(book = book, onClick = { onBookClick(book.key) })
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (book.coverUrl.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📚", style = MaterialTheme.typography.headlineMedium)
                }
            } else {
                SubcomposeAsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier
                        .width(60.dp)
                        .height(90.dp),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📚", style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
