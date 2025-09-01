package com.twugteam.core.domain.run

import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface RunRepository {
    fun getRuns(): Flow<List<Run>>

    // fetched fun from API
    suspend fun fetchRuns(): EmptyResult<DataError>
    suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError>

    suspend fun deleteRunById(id: String)

}