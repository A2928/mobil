package com.example.proj.repository

import com.example.proj.api.ApiModule
import com.example.proj.data.Author
import com.example.proj.data.Book
import com.example.proj.data.BookDetail

class BookRepository {
    private val api = ApiModule.api
    
    suspend fun getBooks(): Result<List<Book>> {
        return try {
            val response = api.getFictionBooks()
            val books = response.works.map { work ->
                Book(
                    key = work.key.replace("/works/", ""),
                    title = work.title,
                    authorNames = work.authors.map { it.name },
                    coverId = work.cover_id
                )
            }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getBookDetail(workId: String): Result<BookDetail> {
        return try {
            val workIdFormatted = if (workId.startsWith("/works/")) {
                workId.replace("/works/", "")
            } else if (!workId.startsWith("OL")) {
                "OL$workId"
            } else {
                workId
            }
            val response = api.getWorkDetail(workIdFormatted)
            val bookDetail = BookDetail(
                key = workId,
                title = response.title,
                authors = response.authors
                    .mapNotNull { authorResponse ->
                        val name = authorResponse.getAuthorName()
                        if (name.isNotBlank()) Author(name) else null
                    },
                numberOfPages = response.number_of_pages,
                publishYear = response.first_publish_year,
                description = response.getDescriptionText(),
                coverId = response.covers?.firstOrNull()
            )
            Result.success(bookDetail)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
