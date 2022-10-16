package com.msa.petsearch.activity.util

import android.R.color.transparent
import android.os.Build
import android.view.View.*
import android.view.WindowInsets
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import androidx.activity.ComponentActivity
import androidx.core.content.res.ResourcesCompat

@Suppress("deprecation")
fun ComponentActivity.deployFullScreenSettings(isSystemInDarkTheme: Boolean) {
    window?.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window?.setDecorFitsSystemWindows(false)

        window?.insetsController?.let {
            it.hide(WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        if (!isSystemInDarkTheme) {
            window?.decorView?.systemUiVisibility = SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    } else {
        if (!isSystemInDarkTheme) {
            window?.decorView?.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        else {
            window?.decorView?.systemUiVisibility =
                SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    window?.statusBarColor = ResourcesCompat.getColor(resources, transparent, theme)
}
