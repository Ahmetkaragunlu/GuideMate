package com.ahmetkaragunlu.guidemate.features.graph

enum class Graph(val route : String) {
    AuthGraph(route = "AuthGraphRoot"),
    GuideGraph(route = "GuideGraphRoot"),
    TouristGraph(route = "TouristGraphRoot"),
}