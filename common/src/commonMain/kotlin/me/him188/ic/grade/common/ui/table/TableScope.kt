package me.him188.ic.grade.common.ui.table

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@TableScopeMarker
class TableScope(
    private val tableState: TableState,
    private val lazyListScope: LazyListScope
) {
    fun row(
        key: Any? = null,
        contentType: Any? = null,
        content: @Composable TableRowScope. () -> Unit
    ) {
        lazyListScope.item(key, contentType) {
            Row(
                Modifier
                    .height(36.dp)
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally)
            ) {
                Spacer(Modifier)
                content(createTableRowScope())
                Spacer(Modifier)
            }
        }
    }

    fun rows(
        count: Int,
        key: ((index: Int) -> Any)? = null,
        contentType: (index: Int) -> Any? = { null },
        itemContent: @Composable TableRowScope.(index: Int) -> Unit
    ) {
        lazyListScope.items(count, key, contentType) { itemIndex ->
            Row(
                Modifier
                    .wrapContentHeight()
                    .border(1.dp, Color.Gray.copy(alpha = 0.3f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterHorizontally)
            ) {
                Spacer(Modifier)
                itemContent(createTableRowScope(), itemIndex)
                Spacer(Modifier)
            }
        }
    }

    private fun createTableRowScope() = object : TableRowScope {
        private var index = 0
        override val tableState: TableState get() = this@TableScope.tableState

        @Composable
        override fun cellImpl(
            modifier: Modifier,
            alignment: Alignment?,
            content: @Composable (BoxScope.(index: Int) -> Unit)
        ) {
            Box(
                Modifier.width(tableState.headers[index].width).then(modifier),
                contentAlignment = alignment ?: Alignment.TopStart
            ) {
                content(index)
            }

            if (index != tableState.headers.lastIndex) {
                Divider(Modifier.padding(vertical = 4.dp).width(1.dp).height(IntrinsicSize.Max))
            }

            index++
        }

        @Composable
        override fun eachCellImpl(
            modifier: Modifier,
            alignment: Alignment?,
            content: @Composable() (BoxScope.(index: Int) -> Unit)
        ) {
            while (index <= tableState.headers.lastIndex) {
                cell(modifier, content = content)
            }
        }
    }
}


@TableScopeMarker
interface TableRowScope {
    val tableState: TableState

    @Composable
    fun cellImpl(
        modifier: Modifier,
        alignment: Alignment?,
        content: @Composable BoxScope.(index: Int) -> Unit
    )

    @Composable
    fun eachCellImpl(
        modifier: Modifier,
        alignment: Alignment?,
        content: @Composable BoxScope.(index: Int) -> Unit
    )
}


@Composable
fun TableRowScope.cell(
    modifier: Modifier = Modifier,
    alignment: Alignment? = null,
    content: @Composable BoxScope.(index: Int) -> Unit
) = cellImpl(modifier, alignment, content)

@Composable
fun TableRowScope.eachCell(
    modifier: Modifier = Modifier,
    alignment: Alignment? = null,
    content: @Composable BoxScope.(index: Int) -> Unit
) = eachCellImpl(modifier, alignment, content)


/**
 * Adds a list of items.
 *
 * @param items the data list
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param itemContent the content displayed by a single item
 */
inline fun <T> TableScope.rows(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline itemContent: @Composable TableRowScope.(item: T) -> Unit
) = rows(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null,
    contentType = { index: Int -> contentType(items[index]) }
) {
    itemContent(items[it])
}

@Deprecated("Use the non deprecated overload", level = DeprecationLevel.HIDDEN)
inline fun <T> TableScope.rows(
    items: List<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable TableRowScope.(item: T) -> Unit
) = rows(items, key, itemContent = itemContent)

/**
 * Adds a list of items where the content of an item is aware of its index.
 *
 * @param items the data list
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param rowContent the content displayed by a single item
 */
inline fun <T> TableScope.rowsIndexed(
    items: List<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline rowContent: @Composable TableRowScope.(index: Int, item: T) -> Unit
) = rows(
    count = items.size,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null,
    contentType = { index -> contentType(index, items[index]) }
) {
    rowContent(it, items[it])
}

/**
 * Adds an array of items.
 *
 * @param rows the data array
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param rowContent the content displayed by a single item
 */
inline fun <T> TableScope.rows(
    rows: Array<T>,
    noinline key: ((item: T) -> Any)? = null,
    noinline contentType: (item: T) -> Any? = { null },
    crossinline rowContent: @Composable TableRowScope.(item: T) -> Unit
) = rows(
    count = rows.size,
    key = if (key != null) { index: Int -> key(rows[index]) } else null,
    contentType = { index: Int -> contentType(rows[index]) }
) {
    rowContent(rows[it])
}

/**
 * Adds an array of items where the content of an item is aware of its index.
 *
 * @param items the data array
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param itemContent the content displayed by a single item
 */
inline fun <T> TableScope.rowsIndexed(
    rows: Array<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline contentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable TableRowScope.(index: Int, item: T) -> Unit
) = rows(
    count = rows.size,
    key = if (key != null) { index: Int -> key(index, rows[index]) } else null,
    contentType = { index -> contentType(index, rows[index]) }
) {
    itemContent(it, rows[it])
}
