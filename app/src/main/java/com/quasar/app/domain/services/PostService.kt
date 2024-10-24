package com.quasar.app.domain.services

import com.quasar.app.domain.models.Post
import com.quasar.app.domain.models.User
import kotlinx.coroutines.flow.Flow

interface PostService {
    suspend fun createPost(user: User, channelId: String, post: CreatePostInput)
    suspend fun deletePost(user: User, postId: String)
    fun getPostsForChannel(channelId: String): Flow<List<Post>>
}

data class CreatePostInput(
    val title: String,
    val content: String
)
