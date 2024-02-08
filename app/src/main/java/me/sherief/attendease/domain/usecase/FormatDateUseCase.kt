package me.sherief.attendease.domain.usecase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDateUseCase(format: String = "yyyy-MM-dd", locale: Locale = Locale.getDefault()) {
    private val dateFormat = SimpleDateFormat(format, locale)
    operator fun invoke(date: Date): String {
        return dateFormat.format(date)
    }
}