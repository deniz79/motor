package com.motorlar.app.data.local

import androidx.room.*
import com.motorlar.app.data.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    
    @Query("SELECT * FROM teams WHERE isActive = 1")
    fun getAllActiveTeams(): Flow<List<Team>>
    
    @Query("SELECT * FROM teams WHERE creatorId = :userId")
    fun getTeamsByCreator(userId: String): Flow<List<Team>>
    
    @Query("SELECT * FROM teams WHERE id = :teamId")
    suspend fun getTeamById(teamId: Long): Team?
    
    @Query("SELECT * FROM teams WHERE isActive = 1 AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')")
    fun searchTeams(query: String): Flow<List<Team>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: Team): Long
    
    @Update
    suspend fun updateTeam(team: Team)
    
    @Delete
    suspend fun deleteTeam(team: Team)
    
    @Query("UPDATE teams SET isActive = :isActive WHERE id = :teamId")
    suspend fun updateTeamActiveStatus(teamId: Long, isActive: Boolean)
    
    @Query("UPDATE teams SET currentRoute = :route WHERE id = :teamId")
    suspend fun updateTeamCurrentRoute(teamId: Long, route: String?)
}
