package com.example.proj.data

data class Book(
    val key: String,
    val title: String,
    val authorNames: List<String> = emptyList(),
    val coverId: Int? = null
) {
    val coverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/$it-M.jpg" }
    
    val author: String
        get() = authorNames.firstOrNull() ?: "Nieznany autor"
}

data class BookDetail(
    val key: String,
    val title: String,
    val authors: List<Author> = emptyList(),
    val numberOfPages: Int? = null,
    val publishYear: Int? = null,
    val description: String? = null,
    val coverId: Int? = null
) {
    val coverUrl: String?
        get() = coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    
    val authorNames: String
        get() = authors.joinToString(", ") { it.name }
}

data class Author(
    val name: String
)
