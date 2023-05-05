package me.him188.ic.grade.web.ui.animation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.CanvasBasedWindow
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.startYearSession
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val session = startYearSession(AcademicYearResult(Computing.Year2))

    onWasmReady {
        CanvasBasedWindow("ImageViewer", requestResize = {
            IntSize(width = 1100, height = 800)
        }) {
            MainWindow(session.yearResult)
        }
    }
}