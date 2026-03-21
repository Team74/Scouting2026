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
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.helper.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreMatchScreen(
    nav: NavHostController,
    state: TabletDataStore
) {

    var primaryColor by remember { mutableStateOf(Color.Transparent) }

    var matchNumber by remember { mutableStateOf("") }
    var teamNumber by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        state.withScouter { primaryColor = it.color() }
        state.withMatch {
            matchNumber = it.match.toString()
            teamNumber = it.team.toString()
        }
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("PreMatch Screen") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor,
                    titleContentColor = Color.White
                )
            )
        }

    ) { innerPadding ->

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    "  Record Match  ",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(221, 171, 159),
                    modifier = Modifier.background(
                        Color(27, 80, 149),
                        shape = MaterialTheme.shapes.medium
                    )
                )
                TextField(
                    matchNumber,
                    onValueChange = { text ->
                        if (text.isDigitsOnly() && text.length < 7) {
                            matchNumber = text
                        }
                    },
                    label = { Text("Match Number") },
                    modifier = Modifier.padding(top = 30.dp, bottom = 30.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)


                )
                TextField(
                    teamNumber,
                    onValueChange = { text ->
                        if (text.isDigitsOnly() && text.length < 7) {
                            teamNumber = text
                        }
                    },
                    label = { Text("Team Number") },
                    modifier = Modifier
                        .padding(top = 30.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }

            Button(
                onClick = {
                    if (
                        !matchNumber.isEmpty() &&
                        matchNumber.isDigitsOnly() &&
                        !teamNumber.isEmpty() &&
                        teamNumber.isDigitsOnly()
                    ) {
                        state.withMatch {
                            state.setMatch(it.copy(
                                match = matchNumber.toInt(),
                                team = teamNumber.toInt()
                            ))
                        }
                        nav.navigate(Screen.Auton)
                    }
                },
                modifier = Modifier,
                shape = RoundedCornerShape(32.dp),
                contentPadding = PaddingValues (100.dp)
            ) {
                Text("Auton", fontSize = 32.sp)
            }

        }
    }

}