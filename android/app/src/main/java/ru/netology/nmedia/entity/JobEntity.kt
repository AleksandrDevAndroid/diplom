package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Job
import kotlin.Long

@Entity(tableName = "job")
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val timeWork: String,
    val position: String,
    val webSite: String,
    val ownerId: Long
) {
    fun toDto() = Job(
        id, name, timeWork, position, webSite, ownerId
    )

    companion object {
        fun fromDto(dto: Job, status: Boolean = true) =
            JobEntity(
                dto.id,
                dto.name,
                dto.timeWork,
                dto.position,
                dto.webSite,
                dto.ownerId
            )
    }
}
