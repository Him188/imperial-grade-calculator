package me.him188.ic.grade.common.result

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow

interface ChangeObservable {
    @Stable
    val changed: Flow<Any?>
}