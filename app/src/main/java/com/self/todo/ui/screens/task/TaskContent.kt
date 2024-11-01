package com.self.todo.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.self.todo.components.PriorityDropDown
import com.self.todo.data.models.Priority
import com.self.todo.util.Constant.MAX_TITLE_LENGTH

@Composable
fun TaskContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { if (it.length <= MAX_TITLE_LENGTH) onTitleChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "Title") }
        )
        Spacer(modifier = Modifier.height(12.dp))
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = onPrioritySelected
        )
        OutlinedTextField(
            value = description,
            onValueChange = { onDescriptionChanged(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp)
                .padding(WindowInsets.ime.asPaddingValues()),
            label = { Text(text = "Description") }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun TaskContentPrev() {
    TaskContent(
        title = "",
        onTitleChanged = { },
        description = "",
        onDescriptionChanged = {},
        priority = Priority.LOW
    ) {

    }
}




