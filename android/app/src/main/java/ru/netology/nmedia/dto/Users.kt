package ru.netology.nmedia.dto

data class Users(
    val id : Long,
    val name: String,
    val login: String,
    val avatar: String?,
    var isSelected: Boolean = false
)
data class UserInfo(
    val name: String,
    val avatar: String?
)