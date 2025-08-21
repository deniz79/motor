package com.motorlar.app.data.model

data class ReelPost(
    val id: Int,
    val username: String,
    val userAvatar: String,
    val location: String,
    val description: String,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val isLiked: Boolean,
    val isVideo: Boolean,
    val videoUrl: String
)
