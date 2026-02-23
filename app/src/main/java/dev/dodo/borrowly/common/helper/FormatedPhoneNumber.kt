package dev.dodo.borrowly.common.helper

fun String.formattedPhoneNumber(): String {
    val cleaned = this.trim()

    return when {
        cleaned.startsWith("0") ->
            "62" + cleaned.drop(1)

        cleaned.startsWith("62") ->
            cleaned

        else ->
            cleaned
    }
}

fun String.formattedVisualPhoneNumber(): String {
    val normalized = this.formattedPhoneNumber()

    return if (normalized.length >= 12) {
        "${normalized.take(5)}-" +
                "${normalized.substring(5, 9)}-" +
                normalized.drop(9)
    } else {
        normalized
    }
}