package me.him188.ic.grade.common

import me.him188.ic.grade.common.persistent.AcademicYearResultData
import me.him188.ic.grade.common.persistent.applyData
import me.him188.ic.grade.common.persistent.json
import me.him188.ic.grade.common.persistent.toData
import me.him188.ic.grade.common.result.AcademicYearResult

internal actual fun deserializeYearResultData(data: String, applyTo: AcademicYearResult) {

    applyTo.applyData(json.decodeFromString(AcademicYearResultData.serializer(), data))
}

internal actual fun serializeYearResultData(data: AcademicYearResult): String {
    return json.encodeToString(AcademicYearResultData.serializer(), data.toData())
}