package me.him188.ic.grade.common

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import me.him188.ic.grade.common.persistent.getPlatformDataManager
import me.him188.ic.grade.common.result.AcademicYearResult
import kotlin.time.Duration.Companion.seconds


class YearSession internal constructor(
    val yearResult: AcademicYearResult,
) {
    private val dataScope: CoroutineScope = CoroutineScope(SupervisorJob())

    private val dataManager = getPlatformDataManager()
    private val savePath = "computing/year2.v1.json"

    @OptIn(FlowPreview::class)
    fun start() {
        val app = this
        app.dataManager.load(app.savePath)
            ?.let { deserializeYearResultData(it, yearResult) }

        app.dataScope.launch {
            yearResult.changed.debounce(10.seconds).collect {
                app.save()
            }
        }

    }

    fun save() {
        dataManager.save(savePath, serializeYearResultData(yearResult))
    }

    fun cancel() {
        this.dataScope.cancel()
    }
}

internal expect fun serializeYearResultData(data: AcademicYearResult): String
internal expect fun deserializeYearResultData(data: String, applyTo: AcademicYearResult)

fun startYearSession(academicYearResult: AcademicYearResult): YearSession {
    return YearSession(academicYearResult).apply { start() }
}