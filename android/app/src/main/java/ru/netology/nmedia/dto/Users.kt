package ru.netology.nmedia.dto

data class Users(
    val id : Long
    val name: String,
    val nickname: String,
    val avatar: String,
    var isSelected: Boolean = false
)
