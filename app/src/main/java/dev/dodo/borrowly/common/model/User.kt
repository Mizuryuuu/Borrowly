package dev.dodo.borrowly.common.model

data class User(
    val idUser: String = "",
    val image: String = "",
    val email: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val password: String = "",
    val preferences: Map<String, List<Int>> = mapOf()
)