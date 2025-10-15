package com.itssagnikmukherjee.swipeassignment.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.itssagnikmukherjee.swipeassignment.R


val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val swipeTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Color.Black.copy(alpha = 0.6f)
    ),
    bodyMedium = TextStyle(
        fontFamily = poppinsFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )
)