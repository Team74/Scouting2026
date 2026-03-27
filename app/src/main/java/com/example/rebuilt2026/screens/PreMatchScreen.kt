package com.example.rebuilt2026.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.rebuilt2026.database.MatchData
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreMatchScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    /* ----------------------------------------------------------------------------------------- */
    // [STATE]
    /* ----------------------------------------------------------------------------------------- */

    // Scouter
    var primaryColor by remember { mutableStateOf(Color.Transparent) }

    // Match state
    var matchNumber by remember { mutableIntStateOf(0) }
    var teamNumber by remember { mutableIntStateOf(0) }

    // Gui state
    var transitioning by remember { mutableStateOf(true) }

    /* ----------------------------------------------------------------------------------------- */
    // [INIT]
    /* ----------------------------------------------------------------------------------------- */

    LaunchedEffect(Unit) {

        // Fetch scouter color
        state.withScouter { primaryColor = it.color() }
        // Fetch current match state
        state.withMatch {
            matchNumber = it.match
            teamNumber = it.team
            // Update at end of init
            transitioning = false
        }

    }

    /* ----------------------------------------------------------------------------------------- */
    // [GUI]
    /* ----------------------------------------------------------------------------------------- */

    Scaffold(
        topBar = { TitleBar(primaryColor) {
            if (!transitioning) {
                transitioning = true
                state.setMatch(MatchData())
                nav.popBackStack()
            }
        } }
    ) { innerPadding ->

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // Uhhhhhh this is doing something I think
            Spacer(modifier = Modifier)

            // Match and team number entries (probably should have gone in their own functions)
            MatchEntry(
                matchNumber = matchNumber,
                teamNumber = teamNumber,
                onMatchEnter = { matchNumber = it },
                onTeamEnter = { teamNumber = it }
            )

            // Continue to next phase button
            Button(
                onClick = {
                    if (!transitioning) {
                        transitioning = true
                        state.withMatch {
                            state.setMatch(
                                it.copy(
                                    match = matchNumber,
                                    team = teamNumber
                                )
                            )
                            nav.navigate(Screen.Auton)
                        }
                    }
                },
                shape = RoundedCornerShape(32.dp),
                contentPadding = PaddingValues (100.dp)
            ) {
                Text("Auton", fontSize = 32.sp)
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(color: Color, onBack: () -> Unit) {

    TopAppBar(
        title = { Text("PreMatch Screen") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color,
            titleContentColor = Color.White
        )
    )

}

@Composable
fun MatchEntry(
    matchNumber: Int,
    teamNumber: Int,
    onMatchEnter: (Int) -> Unit,
    onTeamEnter: (Int) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Record Match",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color(221, 171, 159),
            modifier = Modifier
                .background(Color(27, 80, 149), shape = MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp)
        )

        TextField(
            value = matchNumber.toString(),
            onValueChange = { if (it.isDigitsOnly() && it.length < 7) onMatchEnter(it.toInt()) },
            label = { Text("Match Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(top = 30.dp, bottom = 30.dp)
        )

        TextField(
            value = teamNumber.toString(),
            onValueChange = { if (it.isDigitsOnly() && it.length < 7) onTeamEnter(it.toInt()) },
            label = { Text("Team Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(top = 30.dp)
        )

    }

}





