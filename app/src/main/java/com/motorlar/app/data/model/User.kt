package com.motorlar.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val password: String, // Gerçek uygulamada hash'lenmiş olmalı
    val fullName: String = "",
    val profileImageUrl: String? = null,
    val motorcycleType: MotorcycleType? = null,
    val motorcycleModel: String = "",
    val experienceLevel: String = "BEGINNER", // BEGINNER, INTERMEDIATE, EXPERT
    val totalRides: Int = 0,
    val totalDistance: Double = 0.0, // km
    val totalTime: Long = 0, // minutes
    val averageRating: Float = 0f,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val user: User? = null,
    val token: String? = null
)
