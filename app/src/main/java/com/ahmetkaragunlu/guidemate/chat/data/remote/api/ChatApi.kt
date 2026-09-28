package com.ahmetkaragunlu.guidemate.chat.data.remote.api

import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatConversationResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.request.ClearChatRequest
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessagePageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.ChatMessageResponse
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.request.SendChatMessageRequest
import com.ahmetkaragunlu.guidemate.chat.data.remote.model.response.UnreadCountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("api/v1/chats")
    suspend fun getConversations(): Response<List<ChatConversationResponse>>

    @POST("api/v1/chats/with-user/{remoteUserId}")
    suspend fun findOrCreate(
        @Path("remoteUserId") remoteUserId: Long,
    ): Response<ChatConversationResponse>

    @GET("api/v1/chats/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: String,
        @Query("before") before: String? = null,
        @Query("size") size: Int = 50,
    ): Response<ChatMessagePageResponse>

    @POST("api/v1/chats/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: String,
        @Body request: SendChatMessageRequest,
    ): Response<ChatMessageResponse>

    @POST("api/v1/chats/{chatId}/read")
    suspend fun markRead(
        @Path("chatId") chatId: String,
    ): Response<UnreadCountResponse>

    @POST("api/v1/chats/{chatId}/clear")
    suspend fun clearConversation(
        @Path("chatId") chatId: String,
        @Body request: ClearChatRequest,
    ): Response<UnreadCountResponse>

    @GET("api/v1/chats/unread-count")
    suspend fun getUnreadCount(): Response<UnreadCountResponse>
}
