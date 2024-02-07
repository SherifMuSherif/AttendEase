package me.sherief.attendease.domain.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDateUseCase {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    operator fun invoke(date: Date): String {
        return dateFormat.format(date)
    }
}