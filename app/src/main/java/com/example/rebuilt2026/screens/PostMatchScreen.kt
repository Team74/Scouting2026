package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rebuilt2026.R
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.database.TabletDatabase
import com.example.rebuilt2026.helper.Climb
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostMatchScreen(
    nav: NavHostController,
    db: TabletDatabase,
    state: TabletDataStore
) {

    // Screen state
    var primaryColor by remember { mutableStateOf(Color.Transparent) }

    var climbExpanded by remember { mutableStateOf(false) }
    var endClimb by remember { mutableStateOf(Climb.NO_CLIMB) }

    var matchQuality by remember { mutableIntStateOf(0) }
    var playedDefense by remember { mutableStateOf(false) }
    var didRobotDisable by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }

    // Init state
    LaunchedEffect(Unit) {
        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            endClimb = it.endClimb
            matchQuality = it.matchQuality
            playedDefense = it.playedDefense
            didRobotDisable = it.didRobotDisable
            notes = it.notes
        }
    }

    // User interface
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Match Screen") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            state.withMatch {
                                state.setMatch(it.copy(
                                    endClimb = endClimb,
                                    matchQuality = matchQuality,
                                    playedDefense = playedDefense,
                                    didRobotDisable = didRobotDisable,
                                    notes = notes
                                ))
                            }
                            nav.popBackStack()
                        }
                    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, null, tint = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                ExposedDropdownMenuBox(
                    expanded = climbExpanded,
                    onExpandedChange = { climbExpanded = !climbExpanded }
                ) {
                    OutlinedTextField(
                        value = endClimb.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Climb Level") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = primaryColor // Matching the orange theme
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
                                    endClimb = option
                                    climbExpanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Match Quality?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
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
                                    width = if (matchQuality == index) 4.dp else 0.dp,
                                    color = if (matchQuality == index) primaryColor else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { matchQuality = index },
                            contentScale = ContentScale.Fit
                        )
                    }
                }

            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row {
                    Text("Played Defense?")
                    Switch(
                        checked = playedDefense,
                        onCheckedChange = { playedDefense = it }
                    )
                }

                Row {
                    Text("Did Robot Disable?")
                    Switch(
                        checked = didRobotDisable,
                        onCheckedChange = { didRobotDisable = it }
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(200.dp)
                )

            }

            Button(
                onClick = {
                    state.withMatch {
                        db.matchDataDao().update(it.copy(
                            endClimb = endClimb,
                            matchQuality = matchQuality,
                            playedDefense = playedDefense,
                            didRobotDisable = didRobotDisable,
                            notes = notes
                        ))
                        nav.navigate(Screen.Home) {
                            popUpTo(Screen.Home)
                        }
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .size(128.dp)
            ) {
                Text("Save Match!")
            }

        }

    }

}