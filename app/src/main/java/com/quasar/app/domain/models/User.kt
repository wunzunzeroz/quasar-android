package com.quasar.app.domain.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val channels: List<String> = listOf()
)
