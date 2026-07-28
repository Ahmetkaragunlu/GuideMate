package com.ahmetkaragunlu.guidemate.screens.common.moneyaction.model

data class MoneyActionMethodUi(
    val id: String,
    val title: String,
    val subtitle: String,
    val type: MoneyActionMethodType,
)

enum class MoneyActionMethodType {
    CARD,
    BANK_ACCOUNT,
}
