package com.motorlar.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val creatorId: String,
    val members: List<TeamMember>,
    val currentRoute: Route? = null,
    val isActive: Boolean = true,
    val createdAt: Date = Date(),
    val maxMembers: Int = 10
)

data class TeamMember(
    val userId: String,
    val username: String,
    val role: TeamRole = TeamRole.MEMBER,
    val joinedAt: Date = Date(),
    val isOnline: Boolean = false,
    val currentLocation: Location? = null
)

enum class TeamRole {
    LEADER, MEMBER
}

@Entity(tableName = "team_activities")
data class TeamActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val teamId: Long,
    val routeId: Long?,
    val startTime: Date,
    val endTime: Date? = null,
    val participants: List<String>, // user IDs
    val status: ActivityStatus = ActivityStatus.PLANNED
)

enum class ActivityStatus {
    PLANNED, ACTIVE, COMPLETED, CANCELLED
}
