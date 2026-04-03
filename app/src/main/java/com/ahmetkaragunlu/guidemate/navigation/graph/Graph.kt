package com.ahmetkaragunlu.guidemate.navigation.graph

enum class Graph(
    val route: String,
) {
    AuthGraph(route = "AuthGraphRoot"),
    GuideGraph(route = "GuideGraphRoot"),
    GuideAccountGraph(route = "GuideAccountGraphRoot"),
    TouristGraph(route = "TouristGraphRoot"),
    AccountGraph("AccountGraphRoot"),
}
