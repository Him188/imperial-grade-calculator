import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.theme.AppTheme


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Imperial Grade Calculator",
        state = rememberWindowState(width = 700.dp, height = 800.dp)
    ) {
        AppTheme(true) {
            MainWindow()
        }
    }
}