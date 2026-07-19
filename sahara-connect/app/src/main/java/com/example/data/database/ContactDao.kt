package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM local_contacts ORDER BY name ASC")
    fun getAllLocalContacts(): Flow<List<LocalContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalContact(contact: LocalContactEntity)

    @Delete
    suspend fun deleteLocalContact(contact: LocalContactEntity)

    @Query("SELECT * FROM favorite_overrides")
    fun getAllFavoriteOverrides(): Flow<List<FavoriteOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteOverride(favOverride: FavoriteOverrideEntity)

    @Query("SELECT * FROM call_records ORDER BY timestamp DESC")
    fun getAllCallRecords(): Flow<List<CallRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallRecord(record: CallRecordEntity)

    @Query("DELETE FROM call_records")
    suspend fun clearAllCallRecords()
}
