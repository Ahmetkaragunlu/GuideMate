package com.ahmetkaragunlu.guidemate.chat.di

import com.ahmetkaragunlu.guidemate.chat.data.realtime.ChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.realtime.DefaultChatRealtimeClient
import com.ahmetkaragunlu.guidemate.chat.data.remote.api.ChatApi
import com.ahmetkaragunlu.guidemate.chat.data.repository.ChatRepositoryImpl
import com.ahmetkaragunlu.guidemate.chat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(implementation: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindChatRealtimeClient(
        implementation: DefaultChatRealtimeClient,
    ): ChatRealtimeClient

    companion object {
        @Provides
        @Singleton
        fun provideChatApi(retrofit: Retrofit): ChatApi = retrofit.create(ChatApi::class.java)
    }
}
