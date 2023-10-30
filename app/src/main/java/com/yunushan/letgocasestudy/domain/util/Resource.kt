package com.yunushan.letgocasestudy.domain.util

sealed class Resource<out R> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val error: NetworkError) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
