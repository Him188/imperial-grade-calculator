package me.him188.ic.grade.android

import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Stable
import me.him188.ic.grade.common.Computing
import me.him188.ic.grade.common.MainWindow
import me.him188.ic.grade.common.persistent.FileBasedDataManager
import me.him188.ic.grade.common.persistent.setDataManager
import me.him188.ic.grade.common.result.AcademicYearResult
import me.him188.ic.grade.common.startYearSession
import me.him188.ic.grade.common.theme.AppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dark = true || isSystemInDarkTheme()
            AppTheme(dark) {
                ImmerseStatusBar()
                Scaffold(
                    topBar = { TopAppBar({ Text("Grade Calculator") }) },
                ) {
                    MainWindow(
                        ImperialGradeCalculatorApplication.instance.session.yearResult,
                        useDarkTheme = dark,
                        paddingValues = it,
                    )
                }
            }
        }
    }
}

class ImperialGradeCalculatorApplication : Application() {
    override fun onCreate() {
        setDataManager(FileBasedDataManager(applicationContext.dataDir.toPath()))
        super.onCreate()
    }

    override fun onTerminate() {
        session.save()
        super.onTerminate()
    }

    @Stable
    val academicYearResult by lazy { AcademicYearResult(Computing.Year2) }

    @Stable
    val session by lazy { startYearSession(academicYearResult) }

    init {
        _instance = this
    }

    companion object {
        private lateinit var _instance: ImperialGradeCalculatorApplication

        @Stable
        val instance get() = _instance
    }
}