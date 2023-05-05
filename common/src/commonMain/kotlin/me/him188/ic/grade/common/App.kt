package me.him188.ic.grade.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.him188.ic.grade.common.module.Assessment
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.div
import me.him188.ic.grade.common.numbers.toPercentageString
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.result.AssessmentResult
import me.him188.ic.grade.common.result.availablePercentageInStandaloneModule
import me.him188.ic.grade.common.snackbar.LocalSnackbar
import me.him188.ic.grade.common.theme.AppTheme
import me.him188.ic.grade.common.ui.fundation.OutlinedTextField
import me.him188.ic.grade.common.ui.fundation.onFocusLost
import me.him188.ic.grade.common.ui.fundation.onFocusd
import me.him188.ic.grade.common.ui.fundation.rememberMutableStateOf
import me.him188.ic.grade.common.ui.table.*

@Composable
fun MainWindow(academicYearResult: AcademicYearResult) {
    AppTheme(true) {
        val focusManager = LocalFocusManager.current
        Box(
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(all = 36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            MainWindowContent(academicYearResult)
        }
    }
}

@Composable
private fun MainWindowContent(academicYearResult: AcademicYearResult) {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbar) },
    ) { paddingValues ->
        CompositionLocalProvider(LocalSnackbar provides snackbar) {
            Box(Modifier.padding(paddingValues)) {
                Modules(academicYearResult)
            }
        }
    }
}

@Composable
private fun Modules(academicYearResult: AcademicYearResult) {
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
            header(240.dp) {
                TableHeader("Overall Contribution")
            }
        }
    )
    Table(tableState) {
        for (moduleResult in academicYearResult.moduleResults) {
            val module = moduleResult.module
            summarise(
                name = { Text(module.name) },
                credits = { Text(remember(module.availableCredits) { module.availableCredits.toString() }) },
                grade = {
                    val awardedPercentage by moduleResult.awardedPercentageInThisModule.collectAsState(Percentage.ZERO)
                    Text(describeGrade(awardedPercentage))
                },
                overall = {}
            )

            val indentPadding = 12.dp
            for (submoduleResult in moduleResult.submoduleResults) {
                summarise(
                    name = { Text(submoduleResult.module.name, Modifier.padding(start = indentPadding)) },
                    credits = { },
                    grade = {
                        val awardedPercentage by submoduleResult.awardedPercentageInParentModule.collectAsState(
                            Percentage.ZERO
                        )
                        // e.g. 30%
                        val availablePercentageInParent =
                            remember(submoduleResult) { submoduleResult.module.creditShare }
                        Text(
                            remember(
                                awardedPercentage,
                                availablePercentageInParent
                            ) {
                                // e.g.  25% / 30%
                                "${awardedPercentage.toString(1)} / ${availablePercentageInParent.toString(1)} " +
                                        "(${
                                            (awardedPercentage.value / availablePercentageInParent).toPercentageString(
                                                1
                                            )
                                        })"
                            }
                        )
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

private fun describeGrade(percentage: Percentage): String {
    return percentage.toString() + " - " + GradeLetter.fromMarks(percentage)
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
        cell(alignment = Alignment.Center) { overall() }
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
        var edit by rememberMutableStateOf(TextFieldValue(result.awardedMarks.value?.toString().orEmpty()))
        LaunchedEffect(result) {
            result.awardedMarks.collect {
                val newText = it?.toString().orEmpty()
                if (edit.text != newText) {
                    edit = edit.copy(newText)
                }
            }
        }
        GradeTextField(
            edit,
            { value -> edit = value },
            result.assessment.availableMarks,
            Modifier
                .onFocusd {
                    edit = edit.copy(selection = TextRange(0, edit.text.length)) // select all
                }
                .onFocusLost {
                    val newResult = edit.text.toIntOrNull()?.coerceIn(0, result.assessment.availableMarks)
                    result.setAwardedMarks(newResult)
                    edit = edit.copy(newResult?.toString().orEmpty()) // ensure updated in consecutive errors
                },
        )
    }
    cell { }
}

@Composable
private fun GradeTextField(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    availableMarks: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            text,
            onTextChange,
            Modifier.padding(all = 8.dp).requiredWidth(38.dp).height(32.dp).then(modifier),
            contentPadding = PaddingValues(0.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Text(remember(availableMarks) { "/ $availableMarks" })
    }
}

@Composable
fun TableHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}
