package me.him188.ic.grade.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.him188.ic.grade.common.snackbar.LocalSnackbar
import me.him188.ic.grade.common.ui.fundation.OutlinedTextField
import me.him188.ic.grade.common.ui.fundation.rememberMutableStateOf
import me.him188.ic.grade.common.ui.table.*

@Composable
fun MainWindow() {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbar) },
    ) { paddingValues ->
        val year = remember { Computing.Year2 }

        CompositionLocalProvider(LocalSnackbar provides snackbar) {
            Box(Modifier.padding(paddingValues)) {
                Modules(year)
            }
        }
    }
}

@Composable
private fun Modules(year: AcademicYear) {
    val tableState = rememberTableState(
        buildTableHeaders(defaultCellAlignment = Alignment.Center) {
            header(380.dp, cellAlignment = Alignment.CenterStart) {
                TableHeader("Module")
            }
            header(80.dp) {
                TableHeader("Credits")
            }
            header(120.dp) {
                TableHeader("Grade")
            }
        }
    )
    Table(tableState) {
        for (module in year.modules) {
            summarise(
                name = { Text(module.name) },
                credits = { Text(remember(module.credits) { module.credits.toString() }) },
                grade = { Text("75% - A") }
            )

            val indentPadding = 12.dp
            for (submodule in module.submodules) {
                summarise(
                    name = { Text(submodule.name, Modifier.padding(start = indentPadding)) },
                    credits = { Text(remember(submodule.creditShare) { submodule.creditShare.toString() }) },
                    grade = { }
                )

                rows(submodule.assessments, contentType = { it.category }) { assessment ->
                    assessment(assessment, Modifier.padding(start = indentPadding * 2))
                }
            }

            rows(module.assessments, contentType = { it.category }) { assessment ->
                assessment(assessment, Modifier.padding(start = indentPadding))
            }
        }
    }
}


private fun TableScope.summarise(
    name: @Composable () -> Unit,
    credits: @Composable () -> Unit,
    grade: @Composable () -> Unit,
) {
    row {
        cell(alignment = Alignment.CenterStart) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W700)) {
                name()
            }
        }
        cell(alignment = Alignment.Center) { credits() }
        cell(alignment = Alignment.Center) { grade() }
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
    assessment: Assessment,
    firstCellModifier: Modifier
) {
    cell {
        Text(assessment.name, Modifier.then(firstCellModifier))
    }
    cell(alignment = Alignment.Center) {
        Text(assessment.creditShare.toString())
    }
    cell {
        GradeTextField(assessment.maxGrade)
    }
}

@Composable
private fun GradeTextField(maxGrade: Int) {
    var x by rememberMutableStateOf(TextFieldValue("100"))
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            x,
            {
                x = it
            },
            Modifier.padding(start = 8.dp).padding(vertical = 8.dp).requiredWidth(38.dp).height(32.dp),
            contentPadding = PaddingValues(0.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            singleLine = true,
        )
        Text("/ $maxGrade", Modifier.offset(x = (-8).dp))
    }
}

@Composable
private fun TableHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, fontSize = 18.sp)
}

@Composable
fun StandaloneModuleCard(module: StandaloneModule) {
    ElevatedCard {
        Column(Modifier.padding(all = 16.dp)) {
            Text(module.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            Column {
                for (assessment in module.assessments) {
                    Text(assessment.name)
                }
            }
        }
    }
}
