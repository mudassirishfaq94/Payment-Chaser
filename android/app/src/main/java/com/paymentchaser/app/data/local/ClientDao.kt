package com.paymentchaser.app.data.local

import androidx.room.*
import com.paymentchaser.app.data.model.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients WHERE userId = :userId AND isArchived = 0 ORDER BY name ASC")
    fun getAllActiveClients(userId: String): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    suspend fun getClientById(id: String): ClientEntity?

    @Query("SELECT * FROM clients WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%')")
    fun searchClients(userId: String, query: String): Flow<List<ClientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: ClientEntity)

    @Update
    suspend fun updateClient(client: ClientEntity)

    @Query("UPDATE clients SET isArchived = 1 WHERE id = :id")
    suspend fun archiveClient(id: String)

    @Delete
    suspend fun deleteClient(client: ClientEntity)
}
