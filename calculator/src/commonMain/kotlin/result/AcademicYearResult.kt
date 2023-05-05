package me.him188.ic.grade.common.result

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.merge
import me.him188.ic.grade.common.year.AcademicYear

@Stable
class AcademicYearResult(
    val moduleResults: List<StandaloneModuleResult>,
) : ChangeObservable {
    override val changed = moduleResults.map { it.changed }.merge()
}

fun AcademicYearResult(year: AcademicYear): AcademicYearResult {
    return AcademicYearResult(year.modules.map { StandaloneModuleResult(it) })
}