package dev.forcetower.playtime.core.util

import com.google.gson.JsonDeserializer
import timber.log.Timber
import java.time.LocalDate
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
            Timber.e(e, "Unable to parse ${jsonPrimitive.asString}")
            null
        }
    }
}