package ru.evbobrutskov.tinkoffandroiddeveloper2021.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DevelopersLifePropertyContainer(
    val result: List<DevelopersLifeProperty>
)

@JsonClass(generateAdapter = true)
data class DevelopersLifeProperty(
    val id: Int,
    val description: String,
    @Json(name = "gifURL") val gifUrl: String,
    val width: String,
    val height: String
)