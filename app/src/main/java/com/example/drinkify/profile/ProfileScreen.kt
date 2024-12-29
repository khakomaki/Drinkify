package com.example.drinkify.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Profile", style = MaterialTheme.typography.headlineMedium)

        // name
        Text("Name")
        TextField(
            value = state.name,
            onValueChange = { onEvent(ProfileEvent.UpdateName(it)) },
            placeholder = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth()
        )

        // sex
        Text("Sex")
        TextField(
            value = state.sex,
            onValueChange = { onEvent(ProfileEvent.UpdateSex(it)) },
            placeholder = { Text("Enter your sex") },
            modifier = Modifier.fillMaxWidth()
        )

        // weight
        Text("Weight (kg)")
        TextField(
            value = state.weight.toString(),
            onValueChange = {
                val weight = it.toFloatOrNull() ?: 0f
                onEvent(ProfileEvent.UpdateWeight(weight))
            },
            placeholder = { Text("Enter your weight") },
            modifier = Modifier.fillMaxWidth()
        )

        // save button
        Button(onClick = {
            onEvent(ProfileEvent.SaveProfile)
        }) {
            Text("Save")
        }
    }
}
