package ru.netology.nmedia.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.repository.interfaceRepository.UserRepository
import ru.netology.nmedia.repository.repositoryImp.UserRepositoryImp
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AuthRepositoryModule {
    @Singleton
    @Binds
    fun bindsAuthRepository(iml : UserRepositoryImp) : UserRepository
}