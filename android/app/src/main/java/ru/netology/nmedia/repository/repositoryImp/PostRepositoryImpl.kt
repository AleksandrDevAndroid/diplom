package ru.netology.nmedia.repository.repositoryImp

import android.annotation.SuppressLint
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.api.PostService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.enum.AttachmentType
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.error.NetworkError
import ru.netology.nmedia.error.UnknownError
import ru.netology.nmedia.repository.remoteMediator.PostWallRemoteMediator
import ru.netology.nmedia.repository.interfaceRepository.PostRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val dao: PostDao,
    private val apiService: PostService,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb
) : PostRepository {

    @SuppressLint("CheckResult")
    @OptIn(ExperimentalPagingApi::class)
    override val data: Flow<PagingData<FeedItem>> = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false),
        pagingSourceFactory = { dao.getPagingSource() },
        remoteMediator = PostWallRemoteMediator(apiService, dao, postRemoteKeyDao, appDb)
    ).flow.map { pagingData ->
        pagingData.map(PostEntity::toDto)
    }

    override suspend fun updateStatus() {
        dao.updateStatus()
    }

    override suspend fun save(post: Post) {
        try {
            val response = apiService.savePost(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.Companion.fromDto(body, status = true))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        val oldPost = dao.getId(id)
        dao.removeById(oldPost.id)
        try {
            val response = apiService.deletePost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.insert(PostEntity.fromDto(oldPost.toDto()))
            throw NetworkError
        } catch (e: Exception) {
            dao.insert(PostEntity.fromDto(oldPost.toDto()))
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        val oldPost = dao.getId(id)
        val newPost = oldPost.copy(likedByMe = true, likes = +1)
        dao.insert(PostEntity.Companion.fromDto(newPost.toDto(), status = true))
        try {
            val response = apiService.likesById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body, status = true))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long) {
        val oldPost = dao.getId(id)
        val newPost = oldPost.copy(likedByMe = false, likes = 0)
        dao.insert(PostEntity.fromDto(newPost.toDto(), status = true))
        try {
            val response = apiService.dislikesById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body, status = true))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }


    override suspend fun saveWithAttachment(post: Post, file: File) {
        val media = upload(file)
        val copyPost = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
        save(copyPost)

    }

    private suspend fun upload(file: File?): Media {
        try {
            val part = MultipartBody.Part.createFormData(
                "file", file!!.name, file.asRequestBody()
            )
            val response = apiService.upload(part)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}