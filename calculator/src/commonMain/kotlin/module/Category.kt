package me.him188.ic.grade.common.module

import kotlinx.serialization.Serializable


@Serializable
data class Category(
    val name: String,
) {
    companion object {
        val COURSEWORK = Category("Coursework")
        val EXAMS = Category("Exams")
    }
}