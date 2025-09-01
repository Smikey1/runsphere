@file:OptIn(InternalSerializationApi::class)

package com.twugteam.run.network

import android.util.Log
import com.twugteam.core.data.networking.constructRoute
import com.twugteam.core.data.networking.delete
import com.twugteam.core.data.networking.get
import com.twugteam.core.data.networking.safeCall
import com.twugteam.core.domain.run.RemoteRunDataSource
import com.twugteam.core.domain.run.Run
import com.twugteam.core.domain.util.DataError
import com.twugteam.core.domain.util.EmptyResult
import com.twugteam.core.domain.util.Result
import com.twugteam.core.domain.util.mapToResult
import com.twugteam.run.RunDto
import com.twugteam.run.mappers.toCreateRunRequestJson
import com.twugteam.run.mappers.toRun
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRunRemoteDataSource(
    private val httpClient: HttpClient
) : RemoteRunDataSource {

    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return httpClient.get<List<RunDto>>(
            route = "/runs"
        ).mapToResult { runDtoList ->
            runDtoList.map {
                it.toRun()
            }
        }
    }

    override suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network> {
        val createRunRequestJson = Json.encodeToString(run.toCreateRunRequestJson())
        val result = safeCall<RunDto> {
            Log.d("LOCAL", "postRun---->>: ${mapPicture}")
            httpClient.submitFormWithBinaryData(
                block = { HttpMethod.Post },
                url = constructRoute("/run"),
                formData = formData {
                    append("MAP_PICTURE", mapPicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")

                        // ContentDisposition means --> name of file
                        append(HttpHeaders.ContentDisposition, "filename = mappicture.jpg")
                    })
                    append("RUN_DATA", createRunRequestJson, Headers.build {
                        append(HttpHeaders.ContentType, "text/plain")
                        append(HttpHeaders.ContentDisposition, "form-data; name = \"RUN_DATA\"")
                    })
                }
            )
        }
        return result.mapToResult {
            it.toRun()
        }

    }

    override suspend fun deleteRunById(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = "/run",
            queryParams = mapOf(
                "id" to id
            )
        )
    }
}