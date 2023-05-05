import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.startYearSession

fun main() {
    val academicYearResult = AcademicYearResult(Computing.Year2)
    val session = startYearSession(academicYearResult)

    application {
        Window(
            onCloseRequest = {
                session.save()
                exitApplication()
            },
            title = "Imperial Grade Calculator",
            state = rememberWindowState(width = 1100.dp, height = 800.dp),
        ) {
            MainWindow(academicYearResult, true)
        }
    }
}