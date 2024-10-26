package com.self.todo.data.models

import androidx.compose.ui.graphics.Color
import com.self.todo.ui.theme.highPriorityColor
import com.self.todo.ui.theme.lowPriorityColor
import com.self.todo.ui.theme.mediumPriorityColor
import com.self.todo.ui.theme.nonePriorityColor

enum class Priority(val color: Color) {
    HIGH(highPriorityColor),
    MEDIUM(mediumPriorityColor),
    LOW(lowPriorityColor),
    NONE(nonePriorityColor)
}