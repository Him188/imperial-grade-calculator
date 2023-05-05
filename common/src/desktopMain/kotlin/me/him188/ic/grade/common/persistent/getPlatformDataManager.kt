package me.him188.ic.grade.common.persistent

import dev.dirs.ProjectDirectories
import java.nio.file.Paths


private val projectDirectories: ProjectDirectories by lazy {
    ProjectDirectories.from(
        "me",
        "Him188",
        "Imperial Grade Calculator"
    )
}

private val dataDir = Paths.get(projectDirectories.dataDir)
actual fun getPlatformDataManager(): DataManager = manager

private val manager by lazy { FileBasedDataManager(dataDir) }
