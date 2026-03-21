@file:Suppress("AssignedValueIsNeverRead")

package com.example.rebuilt2026.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rebuilt2026.R
import com.example.rebuilt2026.database.MatchData
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.Scouter
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    var titleText by remember { mutableStateOf(Scouter.NONE.pos) }
    var primaryColor by remember { mutableStateOf(Color.Transparent) }
    var showAdminDialog by remember { mutableStateOf(false) }
    var adminPassword by remember { mutableStateOf("") }

    // One time init on screen load
    LaunchedEffect(Unit) {
        state.withScouter {
            titleText = it.pos
            primaryColor = it.color()
        }
        state.setMatch(MatchData())
    }

    /* ----------------------------------------------------------------------------------------- */
    // [MAIN STRUCTURE]
    /* ----------------------------------------------------------------------------------------- */

    // Above all other widgets
    if (showAdminDialog) {

        BasicAlertDialog(
            onDismissRequest = { showAdminDialog = false }
        ) {
            Card {
                Text(
                    text = "Admin password",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
                OutlinedTextField(
                    value = adminPassword,
                    onValueChange = { adminPassword = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        val password = "yapyapyap"
                        if (adminPassword == password) {
                            nav.navigate(Screen.Admin)
                        }
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
            }
        }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text(text = titleText) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
                )
            )
        }

    ) { innerPadding ->

        // Main Row layout that holds the home screen options and the app logo box
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Left side column that holds the user options
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 16.dp)
            ) {

                // Record a match button
                Button(
                    onClick = { nav.navigate(Screen.PreMatch) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text("Record a Match", fontSize = 55.sp)
                }

                // Nested row layout for little guy and admin button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = 20.dp)
                ) {
                    // Little guy image widget
                    Image(
                        painter = painterResource(id = R.drawable.sillydude_smug),
                        contentScale = ContentScale.Fit,
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp)
                    )
                    // Admin button
                    Button(
                        onClick = { showAdminDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.large,
                    ) { Text("Admin", fontSize = 55.sp) }
                }

                // Data board (subject to change if we don't have time)
                Button(
                    onClick = { /* TODO: Umm idk decide later */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Yippee!",
                        fontSize = 55.sp
                    )
                }

            }

            // Right column app logo
            AppLogo(primaryColor)

        }

    }

}

@Composable
fun AppLogo(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        // Square
        Surface(
            color = color,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 100.dp, bottom = 60.dp)
                .clip(RoundedCornerShape(23.dp))
        ) { }

        // Little guy
        Image(
            painter = painterResource(R.drawable.sillydude_smile),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 260.dp, top = 55.dp, bottom = 20.dp)
                .size(300.dp)
        )

        // Team logo and season title column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 32.dp)
        ) {
            // Team logo
            Image(
                painter = painterResource(R.drawable.team_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 10.dp)
            )
            Text("Rebuilt",
                fontSize = 55.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 60.dp, bottom = 30.dp)
            )
            Text("2026",
                fontSize = 55.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 60.dp, bottom = 30.dp)
            )
        }
    }
}