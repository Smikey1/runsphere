package com.twugteam.core.data.run

import com.twugteam.core.domain.run.LocalRunDataSource
import com.twugteam.core.domain.run.RemoteRunDataSource
import com.twugteam.core.domain.run.Run
import com.twugteam.core.domain.run.RunRepository
import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import com.twugteam.core.domain.util.Result
import com.twugteam.core.domain.util.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }
        val runWithId = run.copy(id = localResult.data) // here data is RunId

        val remoteResult = remoteRunDataSource.postRun(
            run = runWithId,
            mapPicture = mapPicture
        )
        return when (remoteResult) {
            is Result.Error -> {
                // TODO("I will implement sync mechanism later")
                Result.Success(Unit)
            }

            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRunById(id: String) {
        localRunDataSource.deleteRunById(id)

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRunById(id)
        }.await()
    }

}