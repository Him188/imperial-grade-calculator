package me.him188.ic.grade.common.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbar = staticCompositionLocalOf<SnackbarHostState> {
    error("CompositionLocal LocalSnackbar not present")
}