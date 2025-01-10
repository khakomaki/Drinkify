package com.example.drinkify.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drinkify.ui.components.BasicTopBar

@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        BasicTopBar(
            title = "Edit Profile",
            onNavigateBack = onNavigateBack
        )

        // name
        Text("Name")
        TextField(
            value = state.name,
            onValueChange = { onEvent(ProfileEvent.UpdateName(it)) },
            placeholder = { Text("Enter your name") },
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

        // gender
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Gender.entries.forEach { gender ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = state.gender == gender,
                        onClick = { onEvent(ProfileEvent.UpdateGender(gender)) }
                    )
                    Text(gender.toUIVersion())
                }
            }
        }

        // save button
        Button(onClick = {
            onEvent(ProfileEvent.SaveProfile)
        }) {
            Text("Save")
        }
    }
}
