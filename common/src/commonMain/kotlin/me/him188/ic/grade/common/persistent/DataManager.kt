package me.him188.ic.grade.common.persistent

interface DataManager {
    fun save(path: String, data: String)

    fun load(path: String): String?
}

expect fun getPlatformDataManager(): DataManager 
