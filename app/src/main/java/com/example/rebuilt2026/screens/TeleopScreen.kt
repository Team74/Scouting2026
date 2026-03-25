package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var primaryColor by remember { mutableStateOf(Color.Transparent) }
    var navClicked by remember { mutableStateOf(false) }

    var fuelPickup by remember { mutableIntStateOf(0) }
    var inactiveScore by remember { mutableIntStateOf(0) }
    var activeScore by remember { mutableIntStateOf(0) }
    var penalties by remember { mutableIntStateOf(0) }
    var pickupTooMuch by remember { mutableStateOf(false) }

    // Human player state
    var humanScore by remember { mutableIntStateOf(0) }
    var humanPlayerActive by remember { mutableStateOf(false) }
    var humanQuality by remember { mutableIntStateOf(1) }

    val purpleColor = Color(0xFF7449E3)

    LaunchedEffect(Unit) {
        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            fuelPickup = it.teleopFuelPickup
            inactiveScore = it.teleopInactiveScore
            activeScore = it.teleopActiveScore
            penalties = it.teleopPenalties
            pickupTooMuch = it.greatFuelPickup
            humanScore = it.teleopHumanScore
            humanPlayerActive = it.teleopHumanActive
            humanQuality = it.teleopHumanQuality
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
                                    greatFuelPickup = pickupTooMuch,
                                    teleopHumanScore = humanScore,
                                    teleopHumanActive = humanPlayerActive,
                                    teleopHumanQuality = humanQuality
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
                modifier = Modifier.weight(1f).padding(8.dp),
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
                ScoreSystem(
                    label = "Human Player Score",
                    score = humanScore,
                    onScoreChange = { humanScore = it }
                )
            }

            // human player column
            Column(
                modifier = Modifier.weight(1f).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Did the human player do anything?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = humanPlayerActive,
                        onCheckedChange = { humanPlayerActive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = purpleColor,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Did human player do well?",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                                        width = if (humanQuality == index) 4.dp else 0.dp,
                                        color = if (humanQuality == index) purpleColor else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { humanQuality = index }
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }

            // Navigation Column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Was the pickup too much to count?")
                    Switch(
                        checked = pickupTooMuch,
                        onCheckedChange = { pickupTooMuch = it }
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.meow),
                    contentDescription = "Meow",
                    modifier = Modifier
                )
                FilledIconButton(
                    onClick = {
                        if (!navClicked) {
                            state.withMatch {
                                state.setMatch(it.copy(
                                    teleopFuelPickup = fuelPickup,
                                    teleopInactiveScore = inactiveScore,
                                    teleopActiveScore = activeScore,
                                    teleopPenalties = penalties,
                                    greatFuelPickup = pickupTooMuch,
                                    teleopHumanScore = humanScore,
                                    teleopHumanActive = humanPlayerActive,
                                    teleopHumanQuality = humanQuality
                                ))
                            }
                            nav.navigate(Screen.PostMatch)
                            navClicked = true
                        }
                    },
                    modifier = Modifier.size(60.dp),
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
