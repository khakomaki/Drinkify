package com.example.drinkify.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    name: String,
    sex: String,
    weight: Float,
    onSave: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by remember { mutableStateOf(TextFieldValue(name)) }
    var sexInput by remember { mutableStateOf(TextFieldValue(sex)) }
    var weightInput by remember { mutableStateOf(TextFieldValue(weight.toString())) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(text = "Profile", style = MaterialTheme.typography.headlineMedium)

        // name
        Text("Name")
        TextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            placeholder = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )

        // sex
        Text("Sex")
        TextField(
            value = sexInput,
            onValueChange = { sexInput = it },
            placeholder = { Text("Enter your sex") },
            modifier = Modifier.fillMaxWidth()
        )

        // weight
        Text("Weight (kg)")
        TextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            placeholder = { Text("Enter your weight") },
            modifier = Modifier.fillMaxWidth()
        )

        // validation error message
        Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)

        // save button
        Button(onClick = {
            val weightValue = weightInput.text.toFloatOrNull()

            errorMessage = ""

            // validation
            if (weightValue == null || weightValue <= 0) {
                errorMessage += "Set valid value for weight\n"
            }
            if (nameInput.text.isBlank()) {
                errorMessage += "Set name\n"
            }
            if (sexInput.text.isBlank()) {
                errorMessage += "Set sex\n"
            }

            if (errorMessage.toString().isBlank() && weightValue != null) {
                onSave(nameInput.text, sexInput.text, weightValue)
            }
        }) {
            Text("Save")
        }
    }
}
