package com.paymentchaser.app.data.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<FirebaseUser?>
    fun getCurrentUserId(): String?
    fun isUserAuthenticated(): Boolean

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser>
    suspend fun signUpWithEmail(email: String, pass: String): Result<FirebaseUser>
    suspend fun sendPasswordReset(email: String): Result<Unit>
    suspend fun signOut()
    suspend fun deleteAccount(): Result<Unit>
}
