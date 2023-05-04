package me.him188.ic.grade.common.result

import kotlinx.coroutines.flow.Flow

interface ChangeObservable {
    val changed: Flow<Any?>
}