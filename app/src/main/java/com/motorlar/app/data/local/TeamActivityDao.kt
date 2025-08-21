package com.motorlar.app.data.local

import androidx.room.*
import com.motorlar.app.data.model.TeamActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamActivityDao {
    
    @Query("SELECT * FROM team_activities WHERE teamId = :teamId ORDER BY startTime DESC")
    fun getActivitiesByTeam(teamId: Long): Flow<List<TeamActivity>>
    
    @Query("SELECT * FROM team_activities WHERE id = :activityId")
    suspend fun getActivityById(activityId: Long): TeamActivity?
    
    @Query("SELECT * FROM team_activities WHERE status = 'ACTIVE'")
    fun getActiveActivities(): Flow<List<TeamActivity>>
    
    @Query("SELECT * FROM team_activities WHERE teamId = :teamId AND status = 'ACTIVE'")
    suspend fun getActiveActivityByTeam(teamId: Long): TeamActivity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: TeamActivity): Long
    
    @Update
    suspend fun updateActivity(activity: TeamActivity)
    
    @Delete
    suspend fun deleteActivity(activity: TeamActivity)
    
    @Query("UPDATE team_activities SET status = :status WHERE id = :activityId")
    suspend fun updateActivityStatus(activityId: Long, status: String)
    
    @Query("UPDATE team_activities SET endTime = :endTime WHERE id = :activityId")
    suspend fun updateActivityEndTime(activityId: Long, endTime: Long)
}
