package g.sig.questweaver.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val AlegryaFontFamily =
    FontFamily(
        Font(R.font.alegreya, FontWeight.Normal),
        Font(R.font.alegreya_italic, style = FontStyle.Italic),
        Font(R.font.alegreya_black, FontWeight.Bold, loadingStrategy = FontLoadingStrategy.Async),
        Font(R.font.alegreya_bold, FontWeight.Medium, loadingStrategy = FontLoadingStrategy.Async),
    )

private val displayLarge =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
    )

private val displayMedium =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 45.sp,
    )

private val displaySmall =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
    )

private val headlineLarge =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

private val headlineMedium =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )

private val headlineSmall =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
    )

private val titleLarge =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

private val titleMedium =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )

private val titleSmall =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
    )

private val bodyLarge =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
    )

private val bodyMedium =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
    )

private val bodySmall =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
    )

private val labelLarge =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
    )

private val labelMedium =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
    )

private val labelSmall =
    TextStyle(
        fontFamily = AlegryaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
    )

internal val appTypography =
    Typography(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        displaySmall = displaySmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        bodyLarge = bodyLarge,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
        labelLarge = labelLarge,
        labelMedium = labelMedium,
        labelSmall = labelSmall,
    )
