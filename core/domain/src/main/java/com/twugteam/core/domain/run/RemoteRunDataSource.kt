package com.twugteam.core.domain.run

import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import com.twugteam.core.domain.util.Result

interface RemoteRunDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.Network>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network>
    suspend fun deleteRunById(id: String): EmptyResult<DataError.Network>
}