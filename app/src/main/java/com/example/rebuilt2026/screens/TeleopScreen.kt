package com.example.rebuilt2026.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.filledIconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.ScoreSystem
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeleopScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    var primaryColor by remember { mutableStateOf(Color.Transparent) }
    var navClicked by remember { mutableStateOf(false) }

    var fuelPickup by remember { mutableIntStateOf(0) }
    var inactiveScore by remember { mutableIntStateOf(0) }
    var activeScore by remember { mutableIntStateOf(0) }
    var penalties by remember { mutableIntStateOf(0) }
    var pickupTooMuch by remember { mutableStateOf(false) }

    val purpleColor = Color(0xFF7449E3)

    LaunchedEffect(Unit) {
        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            fuelPickup = it.teleopFuelPickup
            inactiveScore = it.teleopInactiveScore
            activeScore = it.teleopActiveScore
            penalties = it.teleopPenalties
            pickupTooMuch = it.greatFuelPickup
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TeleOp Match Screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!navClicked) {
                            state.withMatch {
                                state.setMatch(it.copy(
                                    teleopFuelPickup = fuelPickup,
                                    teleopInactiveScore = inactiveScore,
                                    teleopActiveScore = activeScore,
                                    teleopPenalties = penalties,
                                    greatFuelPickup = pickupTooMuch
                                ))
                            }
                            nav.popBackStack()
                            navClicked = true
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = White
                )
            )
        }
    ) { innerPadding ->
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Scoring Column
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScoreSystem(
                    label = "Fuel Pick Up",
                    score = fuelPickup,
                    onScoreChange = { fuelPickup = it }
                )
                ScoreSystem(
                    label = "Inactive Scoring",
                    score = inactiveScore,
                    onScoreChange = { inactiveScore = it }
                )
                ScoreSystem(
                    label = "Active Scoring",
                    score = activeScore,
                    onScoreChange = { activeScore = it }
                )
                ScoreSystem(
                    label = "Penalties",
                    score = penalties,
                    onScoreChange = { penalties = it }
                )

            }

            // Navigation Column
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Row {
                    Text("Was the pickup too much to count?")
                    Switch(
                        checked = pickupTooMuch,
                        onCheckedChange = { pickupTooMuch = it }
                    )
                }

                FilledIconButton(
                    onClick = {
                        if (!navClicked) {
                            state.withMatch {
                                state.setMatch(it.copy(
                                    teleopFuelPickup = fuelPickup,
                                    teleopInactiveScore = inactiveScore,
                                    teleopActiveScore = activeScore,
                                    teleopPenalties = penalties,
                                    greatFuelPickup = pickupTooMuch
                                ))
                            }
                            nav.navigate(Screen.PostMatch)
                            navClicked = true
                        }
                    },
                    modifier = Modifier.size(128.dp),
                    shape = CircleShape,
                    colors = filledIconButtonColors(
                        containerColor = purpleColor,
                        contentColor = White
                    ),
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }

}