package com.example.a366pi

// Added data class user to hold dummy data
data class User(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)

// Getting the list of users [GET]
data class UserResponse(
    val data: List<User>
)
