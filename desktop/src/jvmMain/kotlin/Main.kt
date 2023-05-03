import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        state = rememberWindowState(width = 760.dp, height = 800.dp),
    ) {
        AppTheme(true) {
            Box(
                Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).padding(all = 36.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                MainWindow()
            }
        }
    }
}