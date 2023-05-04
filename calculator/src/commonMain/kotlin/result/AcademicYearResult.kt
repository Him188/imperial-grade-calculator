package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.merge

class AcademicYearResult(
    val moduleResults: List<StandaloneModuleResult>,
) : ChangeObservable {
    override val changed = moduleResults.map { it.changed }.merge()
}