package me.sherief.attendease.data.util

import android.content.Context
import javax.inject.Inject

open class ResourceManager @Inject constructor(private val context: Context) {
    fun getString(id: Int): String = context.getString(id)

    fun getString(resId: Int, vararg formatArgs: Any?): String {
        return context.getString(resId, *formatArgs)
    }
}