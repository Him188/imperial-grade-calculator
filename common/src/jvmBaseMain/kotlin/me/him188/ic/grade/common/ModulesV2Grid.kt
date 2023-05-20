package me.him188.ic.grade.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.him188.ic.grade.common.result.AcademicYearResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal actual fun ModulesV2Grid(academicYearResult: AcademicYearResult) {
    LazyVerticalStaggeredGrid(
        StaggeredGridCells.Adaptive(480.dp),
//        GridCells.Adaptive(320.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        items(academicYearResult.moduleResults, contentType = { it.submoduleResults.isEmpty() }) {
            ModuleV2(
                it,
                Modifier.animateContentSize().widthIn(320.dp, 480.dp).wrapContentHeight()
            )
        }
    }
}
