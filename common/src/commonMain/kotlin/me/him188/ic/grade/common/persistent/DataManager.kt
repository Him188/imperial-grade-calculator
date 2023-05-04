package me.him188.ic.grade.common.persistent

import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

interface DataManager {
    fun save(path: String, data: ByteArray)

    fun load(path: String): ByteArray?
}

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun <T> DataManager.serializeAndSve(path: String, serializer: KSerializer<T>, data: T) {
    val bytes = json.encodeToString(serializer, data).encodeToByteArray()
    save(path, bytes)
}

fun <T : Any> DataManager.loadAndDeserialize(path: String, serializer: KSerializer<T>): T? {
    val data = load(path) ?: return null
    val text = ByteReadPacket(data).use { it.readText() }
    return json.decodeFromString(serializer, text)
}


expect fun getPlatformDataManager(): DataManager 
