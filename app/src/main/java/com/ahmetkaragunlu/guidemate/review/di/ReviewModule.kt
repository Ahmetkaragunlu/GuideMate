package com.ahmetkaragunlu.guidemate.review.di

import com.ahmetkaragunlu.guidemate.review.data.remote.api.ReviewApi
import com.ahmetkaragunlu.guidemate.review.data.repository.ReviewRepositoryImpl
import com.ahmetkaragunlu.guidemate.review.domain.repository.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {
    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        implementation: ReviewRepositoryImpl,
    ): ReviewRepository

    companion object {
        @Provides
        @Singleton
        fun provideReviewApi(retrofit: Retrofit): ReviewApi =
            retrofit.create(ReviewApi::class.java)
    }
}
