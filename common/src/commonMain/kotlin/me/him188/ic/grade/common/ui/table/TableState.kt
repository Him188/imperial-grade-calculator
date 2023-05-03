package me.him188.ic.grade.common.ui.table

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class TableState(
    val headers: List<TableHeader>,
    val headerHeight: Dp = 36.dp,
)

@Composable
fun rememberTableState(
    headers: List<TableHeader>,
) = remember { TableState(headers) }

