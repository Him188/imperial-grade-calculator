package me.him188.ic.grade.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.him188.ic.grade.common.snackbar.LocalSnackbar
import me.him188.ic.grade.common.ui.fundation.OutlinedTextField
import me.him188.ic.grade.common.ui.fundation.onFocusLost
import me.him188.ic.grade.common.ui.fundation.rememberMutableStateOf
import me.him188.ic.grade.common.ui.table.*

@Composable
fun MainWindow() {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbar) },
    ) { paddingValues ->
        val results = remember { Computing.Year2.modules.map { StandaloneModuleResult(it) } }

        CompositionLocalProvider(LocalSnackbar provides snackbar) {
            Box(Modifier.padding(paddingValues)) {
                Modules(results)
            }
        }
    }
}

@Composable
private fun Modules(modules: List<StandaloneModuleResult>) {
    val tableState = rememberTableState(
        buildTableHeaders(defaultCellAlignment = Alignment.Center) {
            header(380.dp, cellAlignment = Alignment.CenterStart) {
                TableHeader("Module")
            }
            header(80.dp) {
                TableHeader("Credits")
            }
            header(180.dp) {
                TableHeader("Module Grade")
            }
            header(240.dp) {
                TableHeader("Overall Contribution")
            }
        }
    )
    Table(tableState) {
        for (moduleResult in modules) {
            val module = moduleResult.module
            summarise(
                name = { Text(module.name) },
                credits = { Text(remember(module.credits) { module.credits.toString() }) },
                grade = {
                    val modulePercentage by moduleResult.totalPercentage.collectAsState(0.0)
                    Text(describeGrade(modulePercentage))
                },
                overall = {}
            )

            val indentPadding = 12.dp
            for (submoduleResult in moduleResult.submoduleResults) {
                summarise(
                    name = { Text(submoduleResult.module.name, Modifier.padding(start = indentPadding)) },
                    credits = { },
                    grade = {
                        val totalPercentage by submoduleResult.totalPercentage.collectAsState(0.0)
                        val submoduleCreditShare = remember(submoduleResult) { submoduleResult.module.creditShare }
                        Text(
                            remember(
                                totalPercentage,
                                submoduleCreditShare
                            ) {
                                "${totalPercentage.toPercentageString(0)} / ${submoduleCreditShare.toString(0)} " +
                                        "(${(totalPercentage / submoduleCreditShare).toPercentageString(0)})"
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
                        creditShare = {
                            val submoduleCreditShare = remember(submoduleResult) { submoduleResult.module.creditShare }
                            Text(
                                remember(
                                    assessment,
                                    submoduleCreditShare
                                ) { (assessment.creditShare.value * submoduleCreditShare).toPercentageString() }
                            )
                        }
                    )
                }
            }

            rows(moduleResult.assessmentResults, contentType = { it.assessment.category }) { assessment ->
                assessment(assessment, Modifier.padding(start = indentPadding))
            }
        }
    }
}

private fun describeGrade(value: Double): String {
    return value.toPercentageString() + " - " + GradeLetter.fromMarks(value)
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
    assessment: AssessmentResult,
    firstCellModifier: Modifier,
    creditShare: @Composable (AssessmentResult) -> Unit = { Text(assessment.creditShare.toString()) },
) {
    cell {
        Text(assessment.assessment.name, Modifier.then(firstCellModifier))
    }
    cell(alignment = Alignment.Center) {
        creditShare(assessment)
    }
    cell(alignment = Alignment.Center) {
        var edit by rememberMutableStateOf(TextFieldValue())
        LaunchedEffect(assessment) {
            assessment.resultGrade.collect {
                edit = edit.copy(it?.toString().orEmpty())
            }
        }
        GradeTextField(
            edit,
            { value -> edit = value },
            assessment.assessment.maxGrade,
            Modifier.onFocusLost {
                val newResult = edit.text.toIntOrNull()?.coerceIn(0, assessment.assessment.maxGrade)
                assessment.setResult(newResult)
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
    maxGrade: Int,
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
        Text(remember(maxGrade) { "/ $maxGrade" })
    }
}

@Composable
private fun TableHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}
