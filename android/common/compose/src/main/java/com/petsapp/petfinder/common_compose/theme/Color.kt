package com.petsapp.petfinder.common_compose.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.petsapp.petfinder.shared.core_res.CoreR


private val backgroundGray = Color(CoreR.colors.background.color.argb)
private val surfaceWhite = Color(CoreR.colors.surface.color.argb)
private val onBackground = Color(CoreR.colors.onSurface.color.argb)

internal val AppColorScheme = lightColorScheme(
    primary = onBackground,
    secondaryContainer = onBackground,
    onSecondaryContainer = onBackground,
    background = backgroundGray,
    onBackground = onBackground,
    surface = surfaceWhite,
    onSurface = onBackground,
    onSurfaceVariant = onBackground.copy(alpha = 0.75F),
    onTertiary = onBackground.copy(alpha = 0.25F),
)