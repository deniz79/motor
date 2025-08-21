package com.motorlar.app.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val creatorId: Long,
    val creatorName: String = "",
    val motorcycleType: MotorcycleType,
    val startLocation: String = "",
    val endLocation: String = "",
    val waypoints: List<Waypoint> = emptyList(),
    val distance: Double, // km
    val duration: Long, // minutes
    val maxSpeed: Double = 0.0, // km/h
    val avgSpeed: Double = 0.0, // km/h
    val maxLeanAngle: Double = 0.0, // degrees
    val difficulty: RouteDifficulty,
    val isPublic: Boolean = true,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val downloadCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val imageUrl: String? = null
) : Parcelable

@Parcelize
data class Waypoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val speed: Double,
    val leanAngle: Double? = null,
    val altitude: Double? = null
) : Parcelable

enum class RouteDifficulty {
    EASY, MEDIUM, HARD, EXPERT
}
