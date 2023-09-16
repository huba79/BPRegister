package com.example.bpregister.utils

import android.app.AlertDialog
import android.content.res.Configuration
import android.content.res.Resources

object ScreenProps {
    fun getDialogThemeAdvice(resources: Resources): Int {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> return AlertDialog.THEME_DEVICE_DEFAULT_DARK
            Configuration.UI_MODE_NIGHT_NO -> return AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
            else -> return AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
        }
    }
}