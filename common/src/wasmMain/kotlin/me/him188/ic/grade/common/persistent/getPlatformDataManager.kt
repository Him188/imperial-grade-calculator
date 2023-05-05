package me.him188.ic.grade.common.persistent

actual fun getPlatformDataManager(): DataManager {
    return DataManagerJs
}

object DataManagerJs : DataManager {
    override fun save(path: String, data: String) {

    }

    override fun load(path: String): String? {
        return null
    }
}