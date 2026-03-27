package com.example.rebuilt2026.helper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults.filledIconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreSystem(
    label: String,
    score: Int,
    onScoreChange: (Int) -> Unit,
    buttonColor: Color = Color(0xFF7449E3)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Decrement Button
        FilledIconButton(
            onClick = { if (score > 0) onScoreChange(score - 1) },
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            colors = filledIconButtonColors(
                containerColor = buttonColor,
                contentColor = White
            )
        ) {
            Text(
                text = "-",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        OutlinedTextField(
            value = score.toString(),
            onValueChange = { },
            label = { Text(label) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(150.dp),
            enabled = false,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        // Increment Button
        FilledIconButton(
            onClick = { onScoreChange(score + 1) },
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            colors = filledIconButtonColors(
                containerColor = buttonColor,
                contentColor = White
            )
        ) {
            Text(
                text = "+",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
    }
}