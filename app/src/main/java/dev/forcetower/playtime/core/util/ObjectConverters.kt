package dev.forcetower.playtime.core.util

import com.google.gson.JsonDeserializer
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ObjectConverters {
    @JvmStatic
    val LD_DESERIALIZER: JsonDeserializer<LocalDate> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            val string = jsonPrimitive.asString
            val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(string, parser)
        } catch (e: Throwable) {
            null
        }
    }

    @JvmStatic
    val ZDT_DESERIALIZER: JsonDeserializer<ZonedDateTime> = JsonDeserializer { json, _, _ ->
        val jsonPrimitive = json.asJsonPrimitive
        try {
            val string = jsonPrimitive.asString
            val instant = Instant.parse(string)
            ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Throwable) {
            Timber.e(e, "error destructuring")
            null
        }
    }
}
