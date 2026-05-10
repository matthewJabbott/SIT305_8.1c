package com.example.sit305_81c

import retrofit2.http.Body
import retrofit2.http.POST

data class ChatRequest(val username: String, val message: String)
data class ChatResponse(val response: String, val timestamp: String)
interface ChatApiService {
    @POST("chat")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}