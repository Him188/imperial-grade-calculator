package me.him188.ic.grade.common.persistent

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json


internal val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun <T> DataManager.serializeAndSve(path: String, serializer: KSerializer<T>, data: T) {
    save(path, json.encodeToString(serializer, data))
}

fun <T : Any> DataManager.loadAndDeserialize(path: String, serializer: KSerializer<T>): T? {
    val data = load(path) ?: return null
    return json.decodeFromString(serializer, data)
}

object CookieBasedDataManager : DataManager {
    override fun save(path: String, data: String) {
//        window.document.cookie = "$path=$data"
//        val resolve = dataDir.resolve(path)
//        resolve.parent?.createDirectories()
//        resolve.writeText(data)
    }

    override fun load(path: String): String? {
//        window.document.cookie.split(";")
        return null
    }
}

actual fun getPlatformDataManager(): DataManager = CookieBasedDataManager