package com.motorlar.app.data.repository

import com.motorlar.app.data.local.UserDao
import com.motorlar.app.data.model.*
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDao: UserDao
) {
    
    suspend fun registerUser(request: RegisterRequest): AuthResponse {
        return try {
            // Kullanıcı adı kontrolü
            val existingUser = userDao.getUserByUsername(request.username)
            if (existingUser != null) {
                return AuthResponse(false, "Bu kullanıcı adı zaten kullanılıyor")
            }
            
            // Email kontrolü
            val existingEmail = userDao.getUserByEmail(request.email)
            if (existingEmail != null) {
                return AuthResponse(false, "Bu email adresi zaten kullanılıyor")
            }
            
            // Yeni kullanıcı oluştur
            val newUser = User(
                username = request.username,
                email = request.email,
                password = request.password, // Gerçek uygulamada hash'lenmeli
                fullName = request.fullName
            )
            
            val userId = userDao.insertUser(newUser)
            val createdUser = userDao.getUserById(userId)
            
            AuthResponse(true, "Kayıt başarılı", createdUser)
        } catch (e: Exception) {
            AuthResponse(false, "Kayıt sırasında hata oluştu: ${e.message}")
        }
    }
    
    suspend fun loginUser(request: LoginRequest): AuthResponse {
        return try {
            val user = userDao.getUserByEmail(request.email)
            
            if (user == null) {
                return AuthResponse(false, "Kullanıcı bulunamadı")
            }
            
            if (user.password != request.password) { // Gerçek uygulamada hash karşılaştırması
                return AuthResponse(false, "Hatalı şifre")
            }
            
            // Son giriş zamanını güncelle
            userDao.updateLastLogin(user.id)
            
            AuthResponse(true, "Giriş başarılı", user)
        } catch (e: Exception) {
            AuthResponse(false, "Giriş sırasında hata oluştu: ${e.message}")
        }
    }
    
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }
    
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
    
    suspend fun updateUserStats(userId: Long, distance: Double, time: Long) {
        userDao.updateUserStats(userId, distance, time)
    }
    
    fun getAllActiveUsers(): Flow<List<User>> {
        return userDao.getAllActiveUsers()
    }
}
