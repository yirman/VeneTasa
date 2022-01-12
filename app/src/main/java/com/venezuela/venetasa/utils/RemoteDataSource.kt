package com.venezuela.venetasa.utils

abstract class RemoteDataSource {

    protected suspend fun <T> getResult(call: suspend () -> T): Resource<T> {
        return try {
            val response = call()
            Resource.success(response)
        } catch (e: Exception) {
            error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network call has failed for a following reason: $message")
    }

}