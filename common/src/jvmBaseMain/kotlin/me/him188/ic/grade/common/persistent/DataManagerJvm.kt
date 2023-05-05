package me.him188.ic.grade.common.persistent

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText


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

@Suppress("NewApi")
class FileBasedDataManager(
    private val dataDir: Path,
) : DataManager {
    override fun save(path: String, data: String) {
        val resolve = dataDir.resolve(path)
        resolve.parent?.createDirectories()
        resolve.writeText(data)
    }

    override fun load(path: String): String? {
        val resolve = dataDir.resolve(path)
        resolve.parent?.createDirectories()
        return resolve.run {
            if (!exists()) null
            else readText()
        }
    }
}