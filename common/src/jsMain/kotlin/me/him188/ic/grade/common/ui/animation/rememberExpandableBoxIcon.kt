package me.him188.ic.grade.common.ui.animation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
@PublishedApi
internal actual fun rememberExpandableBoxIcon(): Painter {
    return painterResource("icons/expand_content_FILL0_wght400_GRAD0_opsz48.svg")
}