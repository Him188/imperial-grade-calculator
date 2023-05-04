package me.him188.ic.grade.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.result.StandaloneModuleResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainWindow(AcademicYearResult(Computing.Year2.modules.map { StandaloneModuleResult(it) }))
            }
        }
    }
}