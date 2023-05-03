package me.him188.ic.grade.common.ui.table

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
inline fun <T> Table(
    tableState: TableState,
    rows: List<T>,
    crossinline getRow: (T) -> List<@Composable () -> Unit>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .scrollable(scrollState, Orientation.Vertical)
            .scrollable(scrollState, Orientation.Horizontal)
    ) {
        TableRow(tableState, remember(tableState.headers) {
            tableState.headers.map {
                { Box(Modifier.fillMaxSize(), contentAlignment = it.cellAlignment) { it.content() } }
            }
        })

        LazyColumn(userScrollEnabled = false) {
            items(rows, key, contentType) { item ->
                TableRow(tableState, getRow(item))
            }
        }
    }
}

@Composable
inline fun Table(
    tableState: TableState,
    modifier: Modifier = Modifier,
    crossinline content: TableScope.() -> Unit,
) {
    Column(modifier.horizontalScroll(rememberScrollState()).wrapContentSize()) {
        TableRow(tableState, remember(tableState.headers) {
            tableState.headers.map {
                { Box(Modifier.fillMaxSize(), contentAlignment = it.cellAlignment) { it.content() } }
            }
        })

        LazyColumn(userScrollEnabled = false) {
            content(TableScope(tableState, this))
        }
    }
}


@Composable
fun TableRow(tableState: TableState, values: List<@Composable () -> Unit>) {
    Row(
        Modifier
            .requiredHeight(tableState.headerHeight)
            .border(1.dp, Color.Gray.copy(alpha = 0.3f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally)
    ) {
        Spacer(Modifier) // dummy spacer

        values.forEachIndexed { index, s ->
            Box(Modifier.requiredWidth(tableState.headers[index].width)) {
                s()
            }
            if (index != values.lastIndex) {
                Divider(Modifier.padding(vertical = 4.dp).width(1.dp).fillMaxHeight())
            }
        }

        Spacer(Modifier) // dummy spacer
    }
}
