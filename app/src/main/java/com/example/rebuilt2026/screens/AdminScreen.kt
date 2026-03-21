
package com.example.rebuilt2026.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.Scouter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    nav: NavHostController,
    state: TabletDataStore,
    onNukeDatabase: () -> Unit,
    onExportDatabase: () -> Unit
) {

    var selectedScouter by remember { mutableStateOf(Scouter.NONE) }

    LaunchedEffect(Unit) {
        state.withScouter { selectedScouter = it }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Admin") },
                navigationIcon = {
                    IconButton(onClick = {
                        state.setScouter(selectedScouter)
                        nav.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                }
            )
        }

    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Button Row
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Nuke all button
                Button(
                    onClick = onNukeDatabase,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(128.dp)
                ) {
                    Text("Nuke", fontSize = 32.sp)
                }

                // Export database button
                Button(
                    onClick = onExportDatabase,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(128.dp)
                ) {
                    Text("Export", fontSize = 32.sp)
                }

            }

            // Scouter select row
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Scouter.entries.forEach { scouter ->
                    FilterChip(
                        selected = selectedScouter == scouter,
                        onClick = { selectedScouter = scouter },
                        label = { Text(scouter.pos) }
                    )
                }

            }

        }

    }

}