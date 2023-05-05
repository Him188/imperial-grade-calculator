package me.him188.ic.grade.common.ui.grade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.him188.ic.grade.common.GradeLetter
import me.him188.ic.grade.common.numbers.Percentage
import me.him188.ic.grade.common.numbers.percent
import me.him188.ic.grade.common.result.StandaloneModuleResult
import me.him188.ic.grade.common.ui.fundation.OutlinedTextField


@Stable
private val ZERO_GRADE = describeGrade(0.percent, true)

@Stable
fun describeGrade(percentage: Percentage?, isProjected: Boolean): AnnotatedString {
    if (percentage == null) {
        return ZERO_GRADE
    }
    return buildAnnotatedString {
        if (isProjected) {
            pushStyle(SpanStyle(color = Color.Gray))
            append("≈ ")
            pop()
        }
        append(percentage.toString())
        append("  ")
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(GradeLetter.fromMarks(percentage).toString())
        pop()
    }
}


@Composable
fun ModuleGrade(moduleResult: StandaloneModuleResult) {
    val awardedPercentage by moduleResult.awardedPercentageInThisModule.collectAsState(Percentage.ZERO)
    val isProjected by moduleResult.isPercentageProjected.collectAsState(true)
    Text(describeGrade(awardedPercentage, isProjected))
}


@Composable
fun GradeTextField(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    availableMarks: Int,
    modifier: Modifier = Modifier,
    width: Dp = 38.dp,
    height: Dp = 32.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val focus = LocalFocusManager.current
        OutlinedTextField(
            text,
            onTextChange,
            modifier.padding(horizontal = 8.dp).requiredWidth(width).requiredHeight(height),
            contentPadding = PaddingValues(0.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions(
                onDone = { focus.moveFocus(FocusDirection.Next) },
                onGo = { focus.moveFocus(FocusDirection.Next) },
            ),
            shape = RoundedCornerShape(8.dp)
        )
        Text(remember(availableMarks) { "/ $availableMarks" })
    }
}
