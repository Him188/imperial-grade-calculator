import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.startYearSession

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val session = startYearSession(AcademicYearResult(Computing.Year2))

    CanvasBasedWindow("Imperial Grade Calculator") {
        MainWindow(session.yearResult)
    }
}