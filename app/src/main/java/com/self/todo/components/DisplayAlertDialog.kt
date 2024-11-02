package com.self.todo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit
) {

    if (openDialog) {
        AlertDialog(
            title = {
                Text(text = title)
            }, text = {
                Text(text = message)
            },
            shape = RoundedCornerShape(16.dp),
            confirmButton = {
                OutlinedButton(
                    onClick = { onYesClicked() },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),

                    ) {
                    Text(text = "Yes",
                        color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { closeDialog() },
                ) {
                    Text(text = "No",
                        color = MaterialTheme.colorScheme.onSurface)
                }
            },
            onDismissRequest = { closeDialog() })

    }
}

@Preview(showBackground = true)
@Composable
private fun AlertPrev() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DisplayAlertDialog(
            title = "Kotlin",
            message = "Are you sure you want to delete Kotlin",
            openDialog = true,
            closeDialog = { /*TODO*/ }) {

        }
    }
}

/*
* i started
* sdfsad
* */