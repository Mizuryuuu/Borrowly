package dev.dodo.borrowly.common.helper

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
fun Timestamp?.toReadableDate(): String {
    if (this == null) return "-"
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(this.toDate()) // Firebase Timestamp punya method .toDate()
}