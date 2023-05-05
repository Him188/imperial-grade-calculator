package me.him188.ic.grade.common

import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.div
import me.him188.ic.grade.common.numbers.percent
import me.him188.ic.grade.common.numbers.toPercentageString
import me.him188.ic.grade.common.result.*
import me.him188.ic.grade.common.snackbar.LocalSnackbar
import me.him188.ic.grade.common.theme.AppTheme
import me.him188.ic.grade.common.ui.fundation.InformationCard
import me.him188.ic.grade.common.ui.fundation.onFocusLost
import me.him188.ic.grade.common.ui.fundation.onFocusd
import me.him188.ic.grade.common.ui.fundation.rememberMutableStateOf
import me.him188.ic.grade.common.ui.grade.GradeTextField
import me.him188.ic.grade.common.ui.grade.ModuleGrade
import me.him188.ic.grade.common.ui.table.*

@Composable
fun MainWindow(
    academicYearResult: AcademicYearResult,
    useGrid: Boolean,
    useDarkTheme: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(all = 36.dp),
) {
    AppTheme(useDarkTheme) {
        val focusManager = LocalFocusManager.current
        Box(
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            MainWindowContent(academicYearResult, useGrid)
        }
    }
}

@Composable
private fun MainWindowContent(academicYearResult: AcademicYearResult, useGrid: Boolean) {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbar) },
    ) { paddingValues ->
        CompositionLocalProvider(LocalSnackbar provides snackbar) {
            Box(Modifier.padding(paddingValues)) {
                if (useGrid) {
                    ModulesV2Grid(academicYearResult)
                } else {
                    ModulesV2Vertical(academicYearResult)
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewModuleV2() {
    Box(Modifier.background(Color.Gray).size(height = 640.dp, width = 480.dp)) {
        ModuleV2(StandaloneModuleResult(Computing.Year2.modules[1]))
    }
}

@Composable
private fun ModuleV2(moduleResult: StandaloneModuleResult, modifier: Modifier = Modifier) {
    val module = moduleResult.module
    ElevatedCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp)) {
            Text(moduleResult.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W700))


            FlowRow(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InformationCard(3.dp) {
                    Text(remember(module.availableCredits) { module.availableCredits.toString() })
                }
                InformationCard(3.dp) {
                    ModuleGrade(moduleResult)
                }
            }

            Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (assessmentResult in moduleResult.assessmentResults) {
                    AssessmentCard(assessmentResult)
                }
            }

            for (submoduleResult in moduleResult.submoduleResults) {
                Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text(
                        submoduleResult.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500)
                    )
                    SubmodulePercentage(submoduleResult, Modifier.align(Alignment.End))
                }
                Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (assessmentResult in submoduleResult.assessmentResults) {
                        AssessmentCard(assessmentResult)
                    }
                }
            }
        }
    }
}

@Composable
private fun AssessmentCard(
    assessmentResult: AssessmentResult
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
        )
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(0.7f)) {
                    Text(
                        assessmentResult.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.W500,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W600)
                    )
                }

                val awardedPercentage by assessmentResult.awardedPercentage.collectAsState(0.percent)
                val text by remember {
                    derivedStateOf {
                        awardedPercentage?.toString() ?: ""
                    }
                }
                Text(
                    text,
                    Modifier.weight(0.3f),
                    softWrap = false,
                    maxLines = 1,
                    textAlign = TextAlign.End
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InformationCard(10.dp) {
                    Text(assessmentResult.assessment.creditShare.toString())
                }

                AssessmentResultTextField(
                    assessmentResult,
                ) { field, onEdit, availableMarks, focusModifiers ->
                    GradeTextField(
                        field,
                        onEdit,
                        availableMarks,
                        focusModifiers,
                        width = 32.dp,
                        height = 32.dp
                    )
                }
            }

        }
    }
}

@Composable
private fun ModulesV2Vertical(academicYearResult: AcademicYearResult) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(academicYearResult.moduleResults, contentType = { it.submoduleResults.isEmpty() }) {
            ModuleV2(it, Modifier.padding(horizontal = 16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ModulesV2Grid(academicYearResult: AcademicYearResult) {
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

@Composable
private fun ModulesV1(academicYearResult: AcademicYearResult) {
    val tableState = rememberTableState(
        buildTableHeaders(defaultCellAlignment = Alignment.Center) {
            header(380.dp, cellAlignment = Alignment.CenterStart) {
                TableHeader("Module")
            }
            header(80.dp) {
                TableHeader("Credits")
            }
            header(200.dp) {
                TableHeader("Marks")
            }
//            header(240.dp) {
//                TableHeader("Overall Contribution")
//            }
        }
    )
    Table(tableState) {
        for (moduleResult in academicYearResult.moduleResults) {
            val module = moduleResult.module
            summarise(
                name = { Text(module.name) },
                credits = { Text(remember(module.availableCredits) { module.availableCredits.toString() }) },
                grade = {
                    ModuleGrade(moduleResult)
                },
                overall = {}
            )

            val indentPadding = 12.dp
            for (submoduleResult in moduleResult.submoduleResults) {
                summarise(
                    name = { Text(submoduleResult.module.name, Modifier.padding(start = indentPadding)) },
                    credits = { },
                    grade = {
                        SubmodulePercentage(submoduleResult)
                    },
                    overall = {

                    },
                )

                rows(submoduleResult.assessmentResults, contentType = { it.assessment.category }) { assessment ->
                    assessment(
                        assessment,
                        Modifier.padding(start = indentPadding * 2),
                        credits = {
                            Text(
                                remember(
                                    assessment,
                                    submoduleResult
                                ) {
                                    assessment.availablePercentageInStandaloneModule(submoduleResult.module).toString()
                                }
                            )
                        }
                    )
                }
            }

            rows(moduleResult.assessmentResults, contentType = { it.assessment.category }) { assessment ->
                assessment(assessment, Modifier.padding(start = indentPadding))
            }

            row { eachCell { } }
        }
    }
}

@Composable
private fun SubmodulePercentage(submoduleResult: SubmoduleResult, modifier: Modifier = Modifier) {
    val awardedPercentage by submoduleResult.awardedPercentageInParentModule.collectAsState(
        Percentage.ZERO
    )
    // e.g. 30%
    Text(
        // e.g.  25% / 30%
        calculateSubmodulePercentageDisplay(
            awardedPercentage,
            submoduleResult.module.creditShare
        ),
        modifier
    )
}

@Stable
private fun calculateSubmodulePercentageDisplay(
    awardedPercentage: Percentage?,
    availablePercentageInParent: Percentage
): String {
    if (awardedPercentage == null) return ""
    return "${awardedPercentage.toString(1)} / ${availablePercentageInParent.toString(1)} " +
            "(${(awardedPercentage.value / availablePercentageInParent).toPercentageString(1)})"
}


private fun TableScope.summarise(
    name: @Composable () -> Unit,
    credits: @Composable () -> Unit,
    grade: @Composable () -> Unit,
    overall: @Composable () -> Unit,
) {
    row {
        cell(alignment = Alignment.CenterStart) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W700)) {
                name()
            }
        }
        cell(alignment = Alignment.Center) { credits() }
        cell(alignment = Alignment.Center) { grade() }
//        cell(alignment = Alignment.Center) { overall() }
    }
}

private fun TableScope.submodule(
    assessments: List<Assessment>,
    firstCellModifier: Modifier,
) {
    rows(assessments, contentType = { it.category }) { assessment ->
        cell {
            Text(assessment.name, Modifier.then(firstCellModifier))
        }
    }
}

@Composable
private fun TableRowScope.assessment(
    result: AssessmentResult,
    firstCellModifier: Modifier,
    credits: @Composable (AssessmentResult) -> Unit = {
        Text(result.assessment.creditShare.toString())
    },
) {
    cell {
        Text(result.assessment.name, Modifier.then(firstCellModifier))
    }
    cell(alignment = Alignment.Center) {
        credits(result)
    }
    cell(alignment = Alignment.Center) {
        AssessmentResultTextField(result)
    }

//    cell { }
}

@Composable
private fun AssessmentResultTextField(
    assessmentResult: AssessmentResult,
    modifier: Modifier = Modifier,
    content: @Composable (field: TextFieldValue, onEdit: (TextFieldValue) -> Unit, availableMarks: Int, focusModifiers: Modifier) -> Unit = { field, onEdit, availableMarks, focusModifiers ->
        GradeTextField(
            field,
            onEdit,
            availableMarks,
            focusModifiers,
        )
    }
) {
    var edit by rememberMutableStateOf(
        TextFieldValue(
            assessmentResult.awardedMarks.value?.toString().orEmpty()
        )
    )
    LaunchedEffect(assessmentResult) {
        assessmentResult.awardedMarks.collect {
            val newText = it?.toString().orEmpty()
            if (edit.text != newText) {
                edit = edit.copy(newText)
            }
        }
    }
    val focusModifiers = modifier
        .onFocusd {
            edit = edit.copy(selection = TextRange(0, edit.text.length)) // select all
        }
        .onFocusLost {
            val newResult =
                edit.text.toIntOrNull()?.coerceIn(
                    0,
                    assessmentResult.assessment.availableMarks
                )
            assessmentResult.setAwardedMarks(newResult)
            edit = edit.copy(
                newResult?.toString().orEmpty()
            ) // ensure updated in consecutive errors
        }

    content(edit, { edit = it }, assessmentResult.assessment.availableMarks, focusModifiers)
}


@Composable
fun TableHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}
