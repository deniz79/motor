package com.motorlar.app.data.local

import androidx.room.*
import com.motorlar.app.data.model.MotorcycleType
import com.motorlar.app.data.model.Route
import com.motorlar.app.data.model.RouteDifficulty
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    
    @Query("SELECT * FROM routes WHERE isPublic = 1 ORDER BY rating DESC, ratingCount DESC")
    fun getAllPublicRoutes(): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE motorcycleType = :motorcycleType AND isPublic = 1 ORDER BY rating DESC")
    fun getRoutesByMotorcycleType(motorcycleType: MotorcycleType): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE creatorId = :userId ORDER BY createdAt DESC")
    fun getRoutesByUser(userId: String): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE id = :routeId")
    suspend fun getRouteById(routeId: Long): Route?
    
    @Query("SELECT * FROM routes WHERE difficulty = :difficulty AND isPublic = 1 ORDER BY rating DESC")
    fun getRoutesByDifficulty(difficulty: RouteDifficulty): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE isPublic = 1 AND (name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') ORDER BY rating DESC")
    fun searchRoutes(query: String): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE isPublic = 1 ORDER BY downloadCount DESC LIMIT :limit")
    fun getPopularRoutes(limit: Int = 10): Flow<List<Route>>
    
    @Query("SELECT * FROM routes WHERE isPublic = 1 ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentRoutes(limit: Int = 10): Flow<List<Route>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: Route): Long
    
    @Update
    suspend fun updateRoute(route: Route)
    
    @Delete
    suspend fun deleteRoute(route: Route)
    
    @Query("UPDATE routes SET rating = :rating, ratingCount = :ratingCount WHERE id = :routeId")
    suspend fun updateRouteRating(routeId: Long, rating: Float, ratingCount: Int)
    
    @Query("UPDATE routes SET downloadCount = downloadCount + 1 WHERE id = :routeId")
    suspend fun incrementDownloadCount(routeId: Long)
}
