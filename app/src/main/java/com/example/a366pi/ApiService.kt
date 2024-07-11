package com.example.a366pi

// API Methods via retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Added classes to GET and PUSH custom data
data class CreateUserRequest(val employeeFirstname: String, val employeeLastname: String,val employeePosition: String)
data class CreateUserResponse(val id: String, val employeeFirstname: String, val employeeLastname: String,val employeePosition: String, val createdAt: String)

// API interface
interface ApiService {
    @GET("users?page=2")
    suspend fun getUsers(): UserResponse

    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest): CreateUserResponse
}
