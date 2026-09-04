package com.ahmetkaragunlu.guidemate.common.location.locale

import com.ahmetkaragunlu.guidemate.common.location.model.CountryOption
import com.ahmetkaragunlu.guidemate.common.location.model.LanguageOption
import java.text.Collator
import java.util.Locale

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
        REPRESENTATIVE_REGION_BY_LANGUAGE
            .asSequence()
            .map { (code, regionCode) ->
                LanguageOption(
                    code = code,
                    displayName = Locale.forLanguageTag(code).getDisplayLanguage(locale),
                    flagEmoji = regionCode.toFlagEmojiOrNull() ?: DEFAULT_LANGUAGE_ICON,
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

private val REPRESENTATIVE_REGION_BY_LANGUAGE =
    linkedMapOf(
        "af" to "ZA",
        "am" to "ET",
        "ar" to "SA",
        "az" to "AZ",
        "be" to "BY",
        "bg" to "BG",
        "bn" to "BD",
        "bs" to "BA",
        "cs" to "CZ",
        "da" to "DK",
        "de" to "DE",
        "dv" to "MV",
        "dz" to "BT",
        "el" to "GR",
        "en" to "GB",
        "es" to "ES",
        "et" to "EE",
        "fa" to "IR",
        "fi" to "FI",
        "fil" to "PH",
        "fj" to "FJ",
        "fr" to "FR",
        "ga" to "IE",
        "he" to "IL",
        "hi" to "IN",
        "hr" to "HR",
        "hu" to "HU",
        "hy" to "AM",
        "id" to "ID",
        "is" to "IS",
        "it" to "IT",
        "ja" to "JP",
        "ka" to "GE",
        "kk" to "KZ",
        "km" to "KH",
        "ko" to "KR",
        "ky" to "KG",
        "lo" to "LA",
        "lt" to "LT",
        "lv" to "LV",
        "mg" to "MG",
        "mi" to "NZ",
        "mk" to "MK",
        "mn" to "MN",
        "ms" to "MY",
        "mt" to "MT",
        "my" to "MM",
        "ne" to "NP",
        "nl" to "NL",
        "no" to "NO",
        "pl" to "PL",
        "ps" to "AF",
        "pt" to "PT",
        "ro" to "RO",
        "ru" to "RU",
        "si" to "LK",
        "sk" to "SK",
        "sl" to "SI",
        "sm" to "WS",
        "so" to "SO",
        "sq" to "AL",
        "sr" to "RS",
        "sv" to "SE",
        "sw" to "TZ",
        "ta" to "IN",
        "te" to "IN",
        "tg" to "TJ",
        "th" to "TH",
        "tk" to "TM",
        "to" to "TO",
        "tr" to "TR",
        "uk" to "UA",
        "ur" to "PK",
        "uz" to "UZ",
        "vi" to "VN",
        "zh" to "CN",
        "zu" to "ZA",
    )

private fun <T> localeComparator(
    collator: Collator,
    selector: (T) -> String,
): Comparator<T> = Comparator { first, second -> collator.compare(selector(first), selector(second)) }
