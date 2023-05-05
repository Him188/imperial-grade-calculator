package me.him188.ic.grade.common.persistent

import dev.dirs.ProjectDirectories
import java.nio.file.Paths
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText


private val projectDirectories: ProjectDirectories by lazy {
    ProjectDirectories.from(
        "me",
        "Him188",
        "Imperial Grade Calculator"
    )
}

actual fun getPlatformDataManager(): DataManager = DataManagerDesktop

@Suppress("NewApi")
private object DataManagerDesktop : DataManager {
    private val dataDir = Paths.get(projectDirectories.dataDir)
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