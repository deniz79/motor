package com.motorlar.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val profileImageUrl: String? = null,
    val motorcycleType: MotorcycleType? = null,
    val motorcycleModel: String? = null,
    val experienceLevel: ExperienceLevel = ExperienceLevel.BEGINNER,
    val totalDistance: Double = 0.0, // km
    val totalRoutes: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val createdAt: Date = Date(),
    val lastActiveAt: Date = Date(),
    val isOnline: Boolean = false,
    val currentLocation: Location? = null
)

enum class ExperienceLevel {
    BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
}

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
