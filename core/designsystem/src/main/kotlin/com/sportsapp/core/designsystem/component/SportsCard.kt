package com.sportsapp.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.theme.SportsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SportsCardPreview() {
    SportsAppTheme {
        SportsCard(onClick = {}) {
            Text(
                text = "Match Result",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Arsenal vs Chelsea",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}