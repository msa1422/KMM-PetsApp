package com.msa.petsearch.shared.datainfrastructurenetwork.ktor

import com.msa.petsearch.shared.coreutil.resource.Resource
import com.msa.petsearch.shared.coreutil.resource.asResource
import com.msa.petsearch.shared.datainfrastructurenetwork.util.ApiErrorException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.SerializationException

internal suspend inline fun <reified ResultType, ReturnType> HttpClient.safeApiCall(
    builder: HttpRequestBuilder.() -> Unit,
    mapper: (ResultType) -> ReturnType
): Resource<ReturnType> {
    return try {
        request(builder).body<ResultType>().asResource { mapper.invoke(it) }
    } catch (exception: Exception) {
        when (exception) {
            is ApiErrorException,
            is NoTransformationFoundException, is DoubleReceiveException,
            is IllegalArgumentException, is IllegalStateException,
            is ClientRequestException, is ServerResponseException,
            is IOException, is SerializationException -> {
                exception.asResource()
            }
            else -> throw exception
        }
    }
}
