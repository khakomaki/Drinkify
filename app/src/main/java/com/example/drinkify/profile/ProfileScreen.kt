package com.example.drinkify.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.drinkify.ui.components.BasicTopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    state: ProfileState,
    viewModel: ProfileViewModel,
    onEvent: (ProfileEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isModified = viewModel.isModified()
    val isValid = viewModel.isProfileValid()

    // return state on dispose
    DisposableEffect(Unit) {
        onDispose { viewModel.resetState() }
    }

    Scaffold(
        topBar = {
            BasicTopBar(
                title = "Edit Profile",
                onNavigateBack = {
                    onNavigateBack()
                    viewModel.resetState()
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.padding(16.dp)
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(padding)
        ) {
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

            // info message about save state
            val infoMessage = when {
                !isValid -> "Please ensure all fields are filled correctly"
                isModified -> "Unsaved changes"
                else -> "All saved!"
            }
            Text(
                text = infoMessage,
                style = MaterialTheme.typography.bodySmall,
                // red for errors
                color = if (!isValid) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )

            // save button
            Button(
                onClick = {
                    onEvent(ProfileEvent.SaveProfile)
                    CoroutineScope(Dispatchers.Main).launch {
                        snackbarHostState.showSnackbar("Profile saved successfully")
                    }
                },
                // disable if save is not valid
                enabled = viewModel.isProfileSaveValid()
            ) {
                Text("Save")
            }
        }
    }
}
