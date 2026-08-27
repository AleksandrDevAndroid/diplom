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
    val position: String,
    val start: String,
    val finish: String?,
    val webSite: String?,
    val ownerId: Long
) {

    fun toDto() = Job(
        id,
        name,
        position,
        start,
        finish,
        webSite,
        ownerId
    )

    companion object {

        fun fromDto(dto: Job, status: Boolean = true) =
            JobEntity(
                dto.id,
                dto.name,
                dto.position,
                dto.start,
                dto.finish,
                dto.link,
                dto.ownerId
            )
    }
}
