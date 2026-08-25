package com.ahmetkaragunlu.guidemate.chat.data.remote.api

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatConversationResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessagePageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.ChatMessageResponseDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.SendChatMessageRequestDto
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.UnreadCountResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("api/v1/chats")
    suspend fun getConversations(): Response<List<ChatConversationResponseDto>>

    @POST("api/v1/chats/with-user/{remoteUserId}")
    suspend fun findOrCreate(
        @Path("remoteUserId") remoteUserId: Long,
    ): Response<ChatConversationResponseDto>

    @GET("api/v1/chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: String,
        @Query("before") before: String? = null,
        @Query("size") size: Int = 50,
    ): Response<ChatMessagePageResponseDto>

    @POST("api/v1/chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: String,
        @Body request: SendChatMessageRequestDto,
    ): Response<ChatMessageResponseDto>

    @POST("api/v1/chats/{chatId}/read")
    suspend fun markRead(
        @Path("chatId") chatId: String,
    ): Response<UnreadCountResponseDto>

    @GET("api/v1/chats/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponseDto>
}
