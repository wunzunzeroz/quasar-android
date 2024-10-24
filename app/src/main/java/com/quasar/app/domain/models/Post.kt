package com.quasar.app.domain.models

import com.google.firebase.firestore.DocumentId

data class Post(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val author: Author = Author(),
    val timestamp: String = ""
)

data class Author(
    val userId: String = "",
    val name: String = ""
)