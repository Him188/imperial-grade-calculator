package me.him188.ic.grade.common.persistent


private lateinit var manager: FileBasedDataManager

fun setDataManager(fileBasedDataManager: FileBasedDataManager) {
    manager = fileBasedDataManager
}

actual fun getPlatformDataManager(): DataManager = manager