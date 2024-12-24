package com.lorenzovainigli.foodexpirationdates.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import java.util.Locale
import kotlin.jvm.java

enum class Language(val code: String, val label: String) {
    SYSTEM("system", "System"),
    ARABIC("ar", "العربية"),
    CHINESE_TRADITIONAL("zh-TW", "繁體中文"),
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    HINDI("hi", "हिन्दी"),
    INDONESIAN("id", "Bahasa Indonesia"),
    ITALIAN("it", "Italiano"),
    JAPANESE("ja", "日本語"),
    POLISH("pl", "Polski"),
    RUSSIAN("ru", "Русский"),
    SPANISH("es", "Español"),
    TAMIL("ta", "தமிழ்"),
    TURKISH("tr", "Türkçe"),
    VIETNAMESE("vi", "Tiếng Việt");

    companion object {
        fun fromCode(code: String): Language {
            return Language.entries.find { it.code == code } ?: SYSTEM
        }
    }
}

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = if (language == Language.SYSTEM.code) {
            Locale.getDefault()
        } else {
            Locale(language)
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun changeLanguage(context: Context, newLanguage: String) {
        setLocale(context, newLanguage)
        val intent = Intent(context, MainActivity::class.java) // or current activity
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        (context as Activity).finish()
    }
}
