package com.example.localstorageapp.ProtoDataStore

import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object PrefSettingsSerializer : androidx.datastore.core.Serializer<PrefSettings> {
    override val defaultValue: PrefSettings
        get() = PrefSettings()

    override suspend fun readFrom(input: InputStream): PrefSettings {
        return try {
            Json.decodeFromString(
                deserializer = PrefSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: PrefSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = PrefSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}