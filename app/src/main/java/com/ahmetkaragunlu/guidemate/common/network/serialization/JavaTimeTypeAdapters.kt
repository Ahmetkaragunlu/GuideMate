package com.ahmetkaragunlu.guidemate.common.network.serialization

import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeParseException

object InstantTypeAdapter : TypeAdapter<Instant>() {
    override fun write(
        out: JsonWriter,
        value: Instant,
    ) {
        out.value(value.toString())
    }

    override fun read(input: JsonReader): Instant =
        parseJavaTime(typeName = "Instant", value = input.nextString(), parser = Instant::parse)
}

object LocalDateTypeAdapter : TypeAdapter<LocalDate>() {
    override fun write(
        out: JsonWriter,
        value: LocalDate,
    ) {
        out.value(value.toString())
    }

    override fun read(input: JsonReader): LocalDate =
        parseJavaTime(typeName = "LocalDate", value = input.nextString(), parser = LocalDate::parse)
}

private fun <T> parseJavaTime(
    typeName: String,
    value: String,
    parser: (String) -> T,
): T =
    try {
        parser(value)
    } catch (exception: DateTimeParseException) {
        throw JsonParseException("Invalid $typeName value", exception)
    }
