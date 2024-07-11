package com.example.a366pi

// API Methods via retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class CreateUserRequest(val employeeName: String, val employeePost: String)
data class CreateUserResponse(val id: String, val employeeName: String, val employeePost: String, val createdAt: String)

interface ApiService {
    @GET("users?page=2")
    suspend fun getUsers(): UserResponse

    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest): CreateUserResponse
}
