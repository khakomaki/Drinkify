package com.example.drinkify.ui.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun DrinkTimePickerDialog(
    initialTime: Long,
    drinkName: String,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = remember { Calendar.getInstance().apply { timeInMillis = initialTime } }
    val selectedTime = remember { mutableStateOf(calendar.time) }
    val context = LocalContext.current

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedTime.value = calendar.time
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val timePicker = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                selectedTime.value = calendar.time
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    val currentTime = System.currentTimeMillis()
    val timeDifference = currentTime - calendar.timeInMillis

    val recordTimeMessage = remember(selectedTime.value) {
        // future record
        if (timeDifference < 0) {
            val duration = Duration.ofMillis(-timeDifference)
            "In ${duration.toHours()} hours, ${duration.toMinutes() % 60} minutes"
        } else {
            val duration = Duration.ofMillis(timeDifference)
            "${duration.toHours()} hours, ${duration.toMinutes() % 60} minutes ago"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text(drinkName) },
        text = {
            Column {
                // date
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { datePicker.show() }) {
                        Text("Select Date")
                    }
                    Text(DateFormat.getDateInstance().format(selectedTime.value))
                }

                // time
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { timePicker.show() }) {
                        Text("Select Time")
                    }
                    Text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime.value))
                }

                // record time message
                Text(
                    text = recordTimeMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(calendar.timeInMillis)
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
