import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.result.AcademicYearResult
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    val academicYearResult = AcademicYearResult(Computing.Year2)
    onWasmReady {
        Window("Imperial Grade Calculator") {
            Column(modifier = Modifier.fillMaxSize()) {
                MainWindow(academicYearResult, true)
            }
        }
    }
}

