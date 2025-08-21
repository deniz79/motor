package com.motorlar.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.motorlar.app.data.model.*
import java.util.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromMotorcycleType(value: MotorcycleType): String {
        return value.name
    }

    @TypeConverter
    fun toMotorcycleType(value: String): MotorcycleType {
        return MotorcycleType.valueOf(value)
    }

    @TypeConverter
    fun fromRouteDifficulty(value: RouteDifficulty): String {
        return value.name
    }

    @TypeConverter
    fun toRouteDifficulty(value: String): RouteDifficulty {
        return RouteDifficulty.valueOf(value)
    }

    @TypeConverter
    fun fromExperienceLevel(value: ExperienceLevel): String {
        return value.name
    }

    @TypeConverter
    fun toExperienceLevel(value: String): ExperienceLevel {
        return ExperienceLevel.valueOf(value)
    }

    @TypeConverter
    fun fromTeamRole(value: TeamRole): String {
        return value.name
    }

    @TypeConverter
    fun toTeamRole(value: String): TeamRole {
        return TeamRole.valueOf(value)
    }

    @TypeConverter
    fun fromActivityStatus(value: ActivityStatus): String {
        return value.name
    }

    @TypeConverter
    fun toActivityStatus(value: String): ActivityStatus {
        return ActivityStatus.valueOf(value)
    }

    @TypeConverter
    fun fromWaypointList(value: List<Waypoint>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toWaypointList(value: String): List<Waypoint> {
        val listType = object : TypeToken<List<Waypoint>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromTeamMemberList(value: List<TeamMember>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTeamMemberList(value: String): List<TeamMember> {
        val listType = object : TypeToken<List<TeamMember>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromLocation(value: Location?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toLocation(value: String?): Location? {
        return value?.let { gson.fromJson(it, Location::class.java) }
    }
}
