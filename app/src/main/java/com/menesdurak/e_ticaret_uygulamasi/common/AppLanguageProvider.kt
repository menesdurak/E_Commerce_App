package com.menesdurak.e_ticaret_uygulamasi.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AppLanguageProvider() {

    fun setLocale(language: String?) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

}