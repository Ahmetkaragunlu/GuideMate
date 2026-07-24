package com.ahmetkaragunlu.guidemate.screens.common.selection.data

import android.icu.util.ULocale
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.CountryOption
import com.ahmetkaragunlu.guidemate.screens.common.selection.model.LanguageOption
import java.text.Collator
import java.util.Locale


private const val UNDEFINED_LANGUAGE_CODE = "und"
private const val DEFAULT_LANGUAGE_ICON = "🌐"
private const val ISO_REGION_CODE_LENGTH = 2
private const val REGIONAL_INDICATOR_SYMBOL_LETTER_A = 0x1F1E6
object LocaleSelectionCatalog {
    fun countries(locale: Locale): List<CountryOption> =
        Locale.getISOCountries()
            .asSequence()
            .map { code ->
                CountryOption(
                    code = code,
                    displayName = Locale.Builder().setRegion(code).build().getDisplayCountry(locale),
                )
            }
            .filter { it.displayName.isNotBlank() }
            .sortedWith(localeComparator(collator(locale), CountryOption::displayName))
            .toList()

    fun languages(locale: Locale): List<LanguageOption> =
        Locale.getAvailableLocales()
            .asSequence()
            .map(Locale::getLanguage)
            .filter { it.isNotBlank() && it != UNDEFINED_LANGUAGE_CODE }
            .map { Locale.forLanguageTag(it).language }
            .distinct()
            .map { code ->
                LanguageOption(
                    code = code,
                    displayName = Locale.forLanguageTag(code).getDisplayLanguage(locale),
                    flagEmoji = likelyRegionFlagEmoji(code),
                )
            }
            .filter { it.displayName.isNotBlank() }
            .sortedWith(localeComparator(collator(locale), LanguageOption::displayName))
            .toList()

    fun country(
        code: String,
        locale: Locale,
    ): CountryOption? = countries(locale).firstOrNull { it.code.equals(code, ignoreCase = true) }

    fun language(
        code: String,
        locale: Locale,
    ): LanguageOption? = languages(locale).firstOrNull { it.code.equals(code, ignoreCase = true) }

    private fun collator(locale: Locale): Collator = Collator.getInstance(locale)

    private fun likelyRegionFlagEmoji(languageCode: String): String {
        val regionCode =
            ULocale.addLikelySubtags(ULocale.forLanguageTag(languageCode)).country
        return regionCode.toFlagEmojiOrNull() ?: DEFAULT_LANGUAGE_ICON
    }

    private fun String.toFlagEmojiOrNull(): String? {
        val regionCode = uppercase(Locale.ROOT)
        if (regionCode.length != ISO_REGION_CODE_LENGTH || regionCode.any { it !in 'A'..'Z' }) {
            return null
        }
        return regionCode
            .map { letter ->
                String(
                    Character.toChars(
                        REGIONAL_INDICATOR_SYMBOL_LETTER_A + (letter - 'A'),
                    ),
                )
            }
            .joinToString(separator = "")
    }

}

private fun <T> localeComparator(
    collator: Collator,
    selector: (T) -> String,
): Comparator<T> = Comparator { first, second -> collator.compare(selector(first), selector(second)) }
