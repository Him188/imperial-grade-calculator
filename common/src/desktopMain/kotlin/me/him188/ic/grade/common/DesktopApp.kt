package me.him188.ic.grade.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.result.StandaloneModuleResult

@Preview
@Composable
fun AppPreview() {
    MainWindow(AcademicYearResult(Computing.Year2.modules.map { StandaloneModuleResult(it) }))
}