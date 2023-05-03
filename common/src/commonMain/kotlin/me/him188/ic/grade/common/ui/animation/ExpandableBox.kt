package me.him188.ic.grade.common.ui.animation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

enum class ExpandDirection {
    BOTTOM_RIGHT,
    BOTTOM_LEFT,
}


@Composable
inline fun ExpandableBox(
    isExpanded: Boolean,
    crossinline onExpandChange: (isExpanded: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    crossinline bottomBar: @Composable BoxScope.(paddingValues: PaddingValues) -> Unit,
    crossinline content: @Composable BoxScope.() -> Unit
) {
    val endPadding = 24.dp
    val bottomPadding = 8.dp
    Column(
        modifier.width(IntrinsicSize.Min).padding(),
        Arrangement.SpaceBetween
    ) {
        Box(Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)) { content() }

        // like row
        Box(Modifier.fillMaxWidth()) {
            Box(
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp, bottom = bottomPadding)
                    .size(32.dp)
            ) {
                val icon = rememberExpandableBoxIcon()
                if (isExpanded) {
                    IconButton({ onExpandChange(false) }) {
                        Icon(icon, "Collapse")
                    }
                } else {
                    IconButton({ onExpandChange(true) }) {
                        Icon(icon, "Expand")
                    }
                }
            }

            bottomBar(PaddingValues(end = endPadding, bottom = bottomPadding))
        }
    }
}

@PublishedApi
@Composable
internal expect fun rememberExpandableBoxIcon(): Painter