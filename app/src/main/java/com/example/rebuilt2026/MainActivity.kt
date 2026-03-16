package com.example.rebuilt2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rebuilt2026.database.TabletDataStore
import com.example.rebuilt2026.database.TabletDatabase
import com.example.rebuilt2026.database.saveDatabaseToCsv
import com.example.rebuilt2026.database.tabletDatabaseBuilder
import com.example.rebuilt2026.helper.Api22Able
import com.example.rebuilt2026.helper.Screen
import com.example.rebuilt2026.screens.AdminScreen
import com.example.rebuilt2026.screens.AutonScreen
import com.example.rebuilt2026.screens.HomeScreen
import com.example.rebuilt2026.screens.PostMatchScreen
import com.example.rebuilt2026.screens.PreMatchScreen
import com.example.rebuilt2026.screens.TeleopScreen
import com.example.rebuilt2026.ui.theme.Rebuilt2026Theme

class MainActivity : ComponentActivity() {

    /* ----------------------------------------------------------------------------------------- */
    // [PROPERTIES]
    /* ----------------------------------------------------------------------------------------- */

    // App database instance to be initialized in the onCreate function
    private lateinit var tabletDatabase: TabletDatabase
    // App preferences used for tablet position and between-screen state tracking
    private lateinit var tabletState: TabletDataStore
    // Activity launcher for the file selector when exporting the database to csv
    val getContent = registerForActivityResult(ActivityResultContracts.CreateDocument(
        "text/csv"
    )) { uri -> saveDatabaseToCsv(uri, tabletDatabase, application.contentResolver) }

    /* ----------------------------------------------------------------------------------------- */
    // [METHODS]
    /* ----------------------------------------------------------------------------------------- */

    private fun launchExportPathSelector(device: String) {
        getContent.launch("${device}-${Api22Able.getTimestamp()}-rebuilt.csv")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Rebuilt2026Theme {

                /* ----------------------------------------------------------------------------- */
                // [IMPORTANT IDK]
                /* ----------------------------------------------------------------------------- */

                val appContext = LocalContext.current
                val coroutineScope = rememberCoroutineScope()

                /* ----------------------------------------------------------------------------- */
                // [DATABASE]
                /* ----------------------------------------------------------------------------- */

                tabletDatabase = tabletDatabaseBuilder(appContext)
                tabletState = TabletDataStore(LocalContext.current, coroutineScope)

                /* ----------------------------------------------------------------------------- */
                // [NAVIGATION]
                /* ----------------------------------------------------------------------------- */

                // Create the navigation controller for moving between screens
                val navController = rememberNavController()
                // This is the mapping of the Screen enum to each screen composable
                NavHost(navController, Screen.Home) {
                    composable<Screen.Home>         { HomeScreen(navController, tabletDatabase, tabletState) }
                    composable<Screen.PreMatch>     { PreMatchScreen(navController, tabletDatabase, tabletState) }
                    composable<Screen.Auton>        { AutonScreen(navController, tabletDatabase, tabletState) }
                    composable<Screen.Teleop>       { TeleopScreen(navController, tabletDatabase, tabletState) }
                    composable<Screen.PostMatch>    { PostMatchScreen(navController, tabletDatabase, tabletState) }
                    composable<Screen.Admin>        {
                        AdminScreen(navController, tabletDatabase, tabletState) {
                            tabletState.withScouter { launchExportPathSelector(it.name) }
                        }
                    }
                }

            }
        }
    }

}