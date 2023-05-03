package me.him188.ic.grade.common.ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
inline fun HoverActionBox(
    modifier: Modifier = Modifier,
    actionPosition: Alignment = Alignment.CenterEnd,
    crossinline actionContent: @Composable () -> Unit,
    crossinline boxContent: @Composable () -> Unit,
) {
    val labelInteraction = remember { MutableInteractionSource() }
    val isLabelHovered by labelInteraction.collectIsHoveredAsState()

    BoxWithConstraints(
        modifier.hoverable(labelInteraction),
    ) {
        Box(Modifier.matchParentSize(), actionPosition) {
            AnimatedVisibility(isLabelHovered, enter = fadeIn(), exit = fadeOut()) {
                actionContent()
            }
        }

        val width by animateDpAsState(if (isLabelHovered) maxWidth - 32.dp else maxWidth)
        Box(Modifier.width(width).height(maxHeight)) {
            boxContent()
        }
    }
}

@Composable
inline fun HoverCopyBox(
    modifier: Modifier = Modifier,
    noinline onClickCopy: () -> Unit,
    crossinline content: @Composable () -> Unit,
) {
    HoverActionBox(
        modifier,
        actionContent = {
            IconButton(onClickCopy, Modifier.requiredSize(32.dp)) {
                Icon(rememberVectorPainter(Icons.Outlined.ContentCopy), "Copy")
            }
        },
        boxContent = content
    )
}