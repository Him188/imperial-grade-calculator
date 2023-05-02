import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.him188.ic.grade.common.App


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
