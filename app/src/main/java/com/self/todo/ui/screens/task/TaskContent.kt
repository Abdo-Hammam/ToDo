package com.self.todo.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.self.todo.components.PriorityDropDown
import com.self.todo.data.models.Priority
import com.self.todo.ui.theme.systemBarLightColor
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
    var isFocused by remember { mutableStateOf(false) } // متغير لتتبع حالة التركيز

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
            label = { Text(text = "Title") },
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedIndicatorColor = systemBarLightColor
            )
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
                .padding(WindowInsets.ime.asPaddingValues())
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            label = { Text(text = if(isFocused) "Description (Optional)" else "Description") },
            colors = OutlinedTextFieldDefaults.colors().copy(
                focusedIndicatorColor = systemBarLightColor
            ),
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




