package com.twugteam.core.database.run

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Upsert
    suspend fun upsertRun(runEntity: RunEntity)

    @Upsert
    suspend fun upsertRuns(runEntities: List<RunEntity>)

    @Query("select * from runentity order by dateTimeUtc desc")
    fun getAllRun(): Flow<List<RunEntity>>

    @Query("delete from runentity where id = :id")
    fun deleteRunById(id: String)

    @Query("delete from runentity")
    fun deleteAllRun()
}