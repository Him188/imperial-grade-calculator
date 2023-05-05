package me.him188.ic.grade.common.module

import androidx.compose.runtime.Immutable


@Immutable
data class Category(
    val name: String,
) {
    companion object {
        val COURSEWORK = Category("Coursework")
        val EXAMS = Category("Exams")
    }
}