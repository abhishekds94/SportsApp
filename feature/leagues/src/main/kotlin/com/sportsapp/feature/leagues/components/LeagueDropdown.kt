package com.sportsapp.feature.leagues.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.sportsapp.core.designsystem.theme.SportsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeagueDropdown(
    leagues: List<String>,
    selectedLeague: String?,
    onLeagueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    screenEdgePadding: Dp = 16.dp
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedLeague ?: "Select League",
            onValueChange = {},
            readOnly = true,
            label = { Text("League") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        /**
         * ExposedDropdownMenu cannot be wider than the anchor.
         * Use DropdownMenu to allow full screen width.
         */
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenEdgePadding)
                .clip(RoundedCornerShape(12.dp)),
            offset = DpOffset(x = -screenEdgePadding, y = 0.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    leagues.forEach { league ->
                        DropdownMenuItem(
                            text = { Text(text = league, style = MaterialTheme.typography.bodyLarge) },
                            onClick = {
                                onLeagueSelected(league)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun LeagueDropdownPreview() {
    SportsAppTheme {
        LeagueDropdown(
            leagues = listOf("Premier League", "La Liga"),
            selectedLeague = "Premier League",
            onLeagueSelected = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
