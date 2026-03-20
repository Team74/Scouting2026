package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
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
import com.example.rebuilt2026.database.TabletDatabase
import com.example.rebuilt2026.helper.ScoreSystem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeleopScreen(
    nav: NavHostController,
    db: TabletDatabase,
    state: TabletDataStore
) {

    var fuelScore by remember { mutableIntStateOf(0) }
    var inactiveScore by remember { mutableIntStateOf(0) }
    var activeScore by remember { mutableIntStateOf(0) }
    var penaltyScore by remember { mutableIntStateOf(0) }
    var humanScore by remember { mutableIntStateOf(0) }
    var robotMoved by remember { mutableStateOf(false) }
    var humanPlayerScore by remember { mutableStateOf(false) }
    val purpleColor = Color(0xFF7449E3)



    var selectedQuality by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TeleOp Match Screen") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            // Scoring Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScoreSystem(
                    label = "Fuel Pick Up",
                    score = fuelScore,
                    onScoreChange = { fuelScore = it }
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
                    score = penaltyScore,
                    onScoreChange = { penaltyScore = it }
                )
                ScoreSystem(
                    label = "Human Player Score",
                    score = humanScore,
                    onScoreChange = { humanScore = it }
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
                            checkedTrackColor = purpleColor,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }

            // human player colum
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                        checked = humanPlayerScore,
                        onCheckedChange = { humanPlayerScore = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = purpleColor,
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }

                Column {
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
                                        width = if (selectedQuality == index) 4.dp else 0.dp,
                                        color = if (selectedQuality == index) purpleColor else Color.Transparent,
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

            // Navigation Column
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                FilledIconButton(
                    onClick = { },
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