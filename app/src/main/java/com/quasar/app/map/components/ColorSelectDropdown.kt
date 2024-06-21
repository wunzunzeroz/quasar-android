package com.quasar.app.map.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mapbox.maps.extension.style.expressions.dsl.generated.get

@Composable
fun ColorSelectDropdown(initialValue: Color = Color.Magenta, onValueChange: (Color) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initialValue) }

    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Green,
        Color.Cyan,
        Color.Magenta,
        Color.Black,
        Color.White,
        Color.Gray,
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { expanded = true },
        ) {
            Text("Color: ${getColorName(selected)}")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(300.dp)
        ) {
            colors.forEach { color ->
                DropdownMenuItem(onClick = {
                    onValueChange(color)
                    selected = color
                    expanded = false
                }, text = { Text(getColorName(color)) })
            }
        }
    }
}

private fun getColorName(color: Color): String {
    return when (color) {
        Color.Red -> "Red"
        Color.Blue -> "Blue"
        Color.Yellow -> "Yellow"
        Color.Green -> "Green"
        Color.Cyan -> "Cyan"
        Color.Magenta -> "Magenta"
        Color.Black -> "Black"
        Color.White -> "White"
        Color.Gray -> "Gray"
        else -> {
            "Unknown color"
        }
    }
}