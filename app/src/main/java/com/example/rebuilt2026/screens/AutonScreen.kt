package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.filledIconButtonColors
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import com.example.rebuilt2026.helper.Climb
import com.example.rebuilt2026.helper.ScoreSystem
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutonScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    /* ----------------------------------------------------------------------------------------- */
    // [STATE]
    /* ----------------------------------------------------------------------------------------- */

    // Constants
    val orangeColor = Color(0xFFFF5900)

    // Scouter
    var primaryColor by remember { mutableStateOf(Color.Transparent) }

    // Match state
    var fuelPickup by remember { mutableIntStateOf(0) }
    var inactiveScore by remember { mutableIntStateOf(0) }
    var activeScore by remember { mutableIntStateOf(0) }
    var penalties by remember { mutableIntStateOf(0) }
    var selectedClimb by remember { mutableStateOf(Climb.NO_CLIMB) }
    var selectedQuality by remember { mutableIntStateOf(1) }
    var robotMoved by remember { mutableStateOf(false) }

    // Gui state
    var climbExpanded by remember { mutableStateOf(false) }
    var transitioning by remember { mutableStateOf(true) }

    /* ----------------------------------------------------------------------------------------- */
    // [INIT]
    /* ----------------------------------------------------------------------------------------- */

    LaunchedEffect(Unit) {

        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            fuelPickup = it.autonFuelPickup
            inactiveScore = it.autonInactiveScore
            activeScore = it.autonActiveScore
            penalties = it.autonPenalties
            selectedClimb = it.autonClimb
            selectedQuality = it.autonQuality
            robotMoved = it.autonMoved
            // Applied after init
            transitioning = false
        }

    }

    /* ----------------------------------------------------------------------------------------- */
    // [GUI]
    /* ----------------------------------------------------------------------------------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auton Match Screen") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!transitioning) {
                                transitioning = true
                                state.withMatch {
                                    state.setMatch(it.copy(
                                        autonFuelPickup = fuelPickup,
                                        autonInactiveScore = inactiveScore,
                                        autonActiveScore = activeScore,
                                        autonPenalties = penalties,
                                        autonClimb = selectedClimb,
                                        autonQuality = selectedQuality,
                                        autonMoved = robotMoved
                                    ))
                                    nav.popBackStack()
                                }
                            }
                        }
                    ) {
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
        ) {
            // auton and scoring
            Column (
                modifier = Modifier.weight(1f).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ScoreSystem(
                    label = "Fuel Pick Up",
                    score = fuelPickup,
                    onScoreChange = { fuelPickup = it },
                    buttonColor = orangeColor
                )
                ScoreSystem(
                    label = "Inactive Scoring",
                    score = inactiveScore,
                    onScoreChange = { inactiveScore = it },
                    buttonColor = orangeColor
                )
                ScoreSystem(
                    label = "Active Scoring",
                    score = activeScore,
                    onScoreChange = { activeScore = it },
                    buttonColor = orangeColor
                )
                ScoreSystem(
                    label = "Penalties",
                    score = penalties,
                    onScoreChange = { penalties = it },
                    buttonColor = orangeColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Did the robot move?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = robotMoved,
                        onCheckedChange = { robotMoved = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Color(0xFFFF5900),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }
            // climbing list and quality
            Column(
                modifier = Modifier.weight(1f).padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = climbExpanded,
                    onExpandedChange = { climbExpanded = !climbExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedClimb.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Climb Level") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF5900) // Matching the orange theme
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = climbExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    )

                    ExposedDropdownMenu(
                        expanded = climbExpanded,
                        onDismissRequest = { climbExpanded = false }
                    ) {
                        Climb.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = {
                                    selectedClimb = option
                                    climbExpanded = false
                                }
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = "Auton Quality?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
                                        width = if (selectedQuality == index) 4.dp else 0.dp,
                                        color = if (selectedQuality == index) orangeColor else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedQuality = index }
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
            // little dude and next button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (!transitioning) {
                            transitioning = true
                            state.withMatch {
                                state.setMatch(it.copy(
                                    autonMoved = robotMoved,
                                    autonFuelPickup = fuelPickup,
                                    autonInactiveScore = inactiveScore,
                                    autonActiveScore = activeScore,
                                    autonPenalties = penalties,
                                    autonClimb = selectedClimb,
                                    autonQuality = selectedQuality
                                ))
                                nav.navigate(Screen.Teleop)
                            }
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.larry),
                    contentScale = ContentScale.Fit,
                    contentDescription = "",
                    modifier = Modifier
                        .size(420.dp)
                )
                FilledIconButton(
                    onClick = {
                        if (!transitioning) {
                            transitioning = true
                            state.withMatch {
                                state.setMatch(it.copy(
                                    autonMoved = robotMoved,
                                    autonFuelPickup = fuelPickup,
                                    autonInactiveScore = inactiveScore,
                                    autonActiveScore = activeScore,
                                    autonPenalties = penalties,
                                    autonClimb = selectedClimb,
                                    autonQuality = selectedQuality
                                ))
                                nav.navigate(Screen.Teleop)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .padding(bottom = 32.dp),
                    shape = CircleShape,
                    colors = filledIconButtonColors(
                        containerColor = orangeColor,
                        contentColor = White
                    ),
                ) {
                    Icon(contentDescription = "", imageVector = Icons.AutoMirrored.Filled.ArrowForward)
                }
            }
        }
    }

}