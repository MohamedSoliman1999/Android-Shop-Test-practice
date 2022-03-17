package com.example.androidshoptest.mainstate

sealed class MainState<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Idle<T>(data: T? = null) : MainState<T>(data = data)

    class Loading<T>(data: T? = null) : MainState<T>(data = data)

    class Success<T>(data: T) : MainState<T>(data = data)

    class Error<T>(throwable: Throwable, data: T? = null) :
        MainState<T>(data = data, throwable = throwable)
//    data class Users(val user: List<User>) : MainState<T>()
//    data class Error(val error: String?) : MainState<T>()
}
