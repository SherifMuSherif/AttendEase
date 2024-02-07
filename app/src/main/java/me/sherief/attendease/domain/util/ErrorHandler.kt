package me.sherief.attendease.domain.util

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
}