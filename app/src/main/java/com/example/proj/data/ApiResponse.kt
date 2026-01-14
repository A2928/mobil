package com.example.proj.data

data class BooksResponse(
    val works: List<Work>
)

data class Work(
    val key: String,
    val title: String,
    val authors: List<WorkAuthor> = emptyList(),
    val cover_id: Int? = null
)

data class WorkAuthor(
    val name: String
)

data class WorkDetailResponse(
    val title: String,
    val authors: List<AuthorResponse> = emptyList(),
    val number_of_pages: Int? = null,
    val first_publish_year: Int? = null,
    val description: Any? = null,
    val covers: List<Int>? = null
) {
    fun getDescriptionText(): String? {
        return when (description) {
            is String -> description
            is Map<*, *> -> description["value"] as? String
            else -> null
        }
    }
}

data class AuthorResponse(
    val author: Map<String, Any>? = null,
    val name: String? = null
) {
    fun getAuthorName(): String {
        return name?.takeIf { it.isNotBlank() } 
            ?: "Nieznany autor"
    }
}
