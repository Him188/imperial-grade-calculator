package me.him188.ic.grade.common.ui.fundation

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.onFocusLost(action: () -> Unit): Modifier {
    return onFocusChanged {
        if (!it.isFocused && !it.hasFocus) {
            action()
        }
    }
}
