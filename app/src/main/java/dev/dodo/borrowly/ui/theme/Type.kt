package dev.dodo.borrowly.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import dev.dodo.borrowly.R

val plus_jakarta_sans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.W400),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.W500),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.W600),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.W700),
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.W800),

    )

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = plus_jakarta_sans),
    displayMedium = TextStyle(fontFamily = plus_jakarta_sans),
    displaySmall = TextStyle(fontFamily = plus_jakarta_sans),
    headlineLarge = TextStyle(fontFamily = plus_jakarta_sans),
    headlineMedium = TextStyle(fontFamily = plus_jakarta_sans),
    headlineSmall = TextStyle(fontFamily = plus_jakarta_sans),
    titleLarge = TextStyle(fontFamily = plus_jakarta_sans),
    titleMedium = TextStyle(fontFamily = plus_jakarta_sans),
    titleSmall = TextStyle(fontFamily = plus_jakarta_sans),
    bodyLarge = TextStyle(fontFamily = plus_jakarta_sans),
    bodyMedium = TextStyle(fontFamily = plus_jakarta_sans),
    bodySmall = TextStyle(fontFamily = plus_jakarta_sans),
    labelLarge = TextStyle(fontFamily = plus_jakarta_sans),
    labelMedium = TextStyle(fontFamily = plus_jakarta_sans),
    labelSmall = TextStyle(fontFamily = plus_jakarta_sans)
)