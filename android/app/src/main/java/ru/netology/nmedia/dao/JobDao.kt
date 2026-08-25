package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.dto.Job
import ru.netology.nmedia.entity.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM job WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getJobs(ownerId: Long): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobEntity>)

    @Delete
    suspend fun delete(job: JobEntity)


}