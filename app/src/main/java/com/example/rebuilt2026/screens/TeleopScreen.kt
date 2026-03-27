package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rebuilt2026.R
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.ScoreSystem
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeleopScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    /* ----------------------------------------------------------------------------------------- */
    // [STATE]
    /* ----------------------------------------------------------------------------------------- */

    // Constants
    val purpleColor = Color(0xFF7449E3)

    // Scouter
    var primaryColor by remember { mutableStateOf(Color.Transparent) }

    // Match state
    var fuelPickup by remember { mutableIntStateOf(0) }
    var inactiveScore by remember { mutableIntStateOf(0) }
    var activeScore by remember { mutableIntStateOf(0) }
    var penalties by remember { mutableIntStateOf(0) }
    var pickupTooMuch by remember { mutableStateOf(false) }

    // Gui state
    var transitioning by remember { mutableStateOf(true) }

    // State that I can't do anything with
    var didHumanPlayerDoAnything by remember { mutableStateOf(false) }
    var didHumanPlayerDoGood by remember { mutableIntStateOf(0) }

    /* ----------------------------------------------------------------------------------------- */
    // [INIT]
    /* ----------------------------------------------------------------------------------------- */

    LaunchedEffect(Unit) {

        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            fuelPickup = it.teleopFuelPickup
            inactiveScore = it.teleopInactiveScore
            activeScore = it.teleopActiveScore
            penalties = it.teleopPenalties
            pickupTooMuch = it.greatFuelPickup
            // Update on init end
            transitioning = false
        }

    }

    /* ----------------------------------------------------------------------------------------- */
    // [GUI]
    /* ----------------------------------------------------------------------------------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TeleOp Match Screen") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!transitioning) {
                            transitioning = true
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

            // Misc column
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                // Pickup / shooting too much decision
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Was the pickup / fuel scored\ntoo much to count?")
                    Switch(
                        checked = pickupTooMuch,
                        onCheckedChange = { pickupTooMuch = it }
                    )
                }

                // Did human player do anything
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Was human player accurate\nif applicable?")
                    Switch(
                        checked = didHumanPlayerDoAnything,
                        onCheckedChange = { didHumanPlayerDoAnything = it }
                    )
                }

                Text(text = "Did human player do well?")

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val drawables = listOf(
                        R.drawable.smiley_bad,
                        R.drawable.smiley_meh,
                        R.drawable.smiley_good
                    )

                    drawables.forEachIndexed { index, drawableId ->
                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = "Quality option $index",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (didHumanPlayerDoGood == index) 4.dp else 0.dp,
                                    color = if (didHumanPlayerDoGood == index) purpleColor else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { didHumanPlayerDoGood = index }
                                .padding(4.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

            }

            // Navigation funny
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (!transitioning) {
                            transitioning = true
                            state.withMatch {
                                state.setMatch(it.copy(
                                    teleopFuelPickup = fuelPickup,
                                    teleopInactiveScore = inactiveScore,
                                    teleopActiveScore = activeScore,
                                    teleopPenalties = penalties,
                                    greatFuelPickup = pickupTooMuch
                                ))
                                nav.navigate(Screen.PostMatch)
                            }
                        }
                    }
            ) {

                Image(
                    painterResource(R.drawable.meow), null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(200.dp)
                )
                Icon(Icons.AutoMirrored.Default.ArrowForward, null, tint = White)

            }
        }
    }

}