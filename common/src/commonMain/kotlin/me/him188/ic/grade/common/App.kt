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
import me.him188.ic.grade.common.snackbar.LocalSnackbar
import me.him188.ic.grade.common.ui.fundation.rememberMutableStateOf
import me.him188.ic.grade.common.ui.table.*

@Composable
fun MainWindow() {
    val snackbar = remember { SnackbarHostState() }
    Scaffold(
        Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).padding(all = 36.dp),
        snackbarHost = { SnackbarHost(snackbar) },
    ) {
        val year = remember { Computing.Year2 }

        CompositionLocalProvider(LocalSnackbar provides snackbar) {
            Modules(year)
        }
    }
}

@Composable
private fun Modules(year: AcademicYear) {
    val tableState = rememberTableState(
        buildTableHeaders(defaultCellAlignment = Alignment.Center) {
            header(320.dp, cellAlignment = Alignment.CenterStart) {
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
            row {
                cell(alignment = Alignment.CenterStart) {
                    Text(
                        module.name,
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                cell(alignment = Alignment.Center) { Text(remember(module.credits) { module.credits.toString() }) }
                cell(alignment = Alignment.Center) { Text("75% - A") }
            }
            rows(module.assessments, contentType = { it.category }) { assessment ->
                cell {
                    Text(assessment.name, Modifier.padding(start = 12.dp))
                }
                cell(alignment = Alignment.Center) {
                    Text(assessment.creditShare.toString())
                }
                cell {
                    var x by rememberMutableStateOf(TextFieldValue("100"))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        me.him188.ic.grade.common.ui.fundation.OutlinedTextField(
                            x,
                            {
                                x = it
                            },
                            Modifier.padding(start = 8.dp).padding(vertical = 8.dp).requiredWidth(38.dp).height(32.dp),
                            contentPadding = PaddingValues(0.dp),
                            textStyle = TextStyle(textAlign = TextAlign.Center),
                            singleLine = true,
                        )
                        Text("/ ${assessment.maxGrade}", Modifier.offset(x = (-8).dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TableHeader(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
