package com.motorlar.app.data.local

import androidx.room.*
import com.motorlar.app.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): User?
    
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE isOnline = 1")
    fun getOnlineUsers(): Flow<List<User>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("UPDATE users SET isOnline = :isOnline, lastActiveAt = :timestamp WHERE id = :userId")
    suspend fun updateUserOnlineStatus(userId: String, isOnline: Boolean, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE users SET currentLocation = :location WHERE id = :userId")
    suspend fun updateUserLocation(userId: String, location: String)
    
    @Query("UPDATE users SET totalDistance = totalDistance + :distance WHERE id = :userId")
    suspend fun updateUserTotalDistance(userId: String, distance: Double)
    
    @Query("UPDATE users SET totalRoutes = totalRoutes + 1 WHERE id = :userId")
    suspend fun incrementUserTotalRoutes(userId: String)
}
