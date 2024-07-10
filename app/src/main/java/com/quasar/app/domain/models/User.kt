package com.quasar.app.domain.models

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val channels: List<String> = listOf()
)
