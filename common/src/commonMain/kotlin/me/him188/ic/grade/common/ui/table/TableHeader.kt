package me.him188.ic.grade.common.ui.table

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp

@Immutable
class TableHeader(
    val width: Dp,
    val cellAlignment: Alignment,
    val content: @Composable () -> Unit,
)

class TableHeaderBuilder(
    private val defaultCellAlignment: Alignment = Alignment.CenterStart,
) {
    private val headers = mutableListOf<TableHeader>()
    fun header(
        width: Dp,
        name: String,
        cellAlignment: Alignment = this.defaultCellAlignment,
    ) {
        headers.add(TableHeader(width, cellAlignment) { Text(name) })
    }

    fun header(
        width: Dp,
        cellAlignment: Alignment = this.defaultCellAlignment,
        content: @Composable () -> Unit
    ) {
        headers.add(TableHeader(width, cellAlignment) { content() })
    }

    fun build() = headers.toList()
}


inline fun buildTableHeaders(
    defaultCellAlignment: Alignment = Alignment.Center,
    action: TableHeaderBuilder.() -> Unit,
): List<TableHeader> = TableHeaderBuilder(defaultCellAlignment).apply(action).build()
