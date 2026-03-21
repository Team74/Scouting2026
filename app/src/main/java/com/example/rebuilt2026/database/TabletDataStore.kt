package com.example.rebuilt2026.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.rebuilt2026.helper.Scouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class TabletDataStore(
    private val context: Context,
    private val coroutine: CoroutineScope
) {

    /* ----------------------------------------------------------------------------------------- */
    // [INITIALIZATION]
    /* ----------------------------------------------------------------------------------------- */

    // Data class to be storing
    @Serializable
    data class TabletDataModel(
        val scouting: Scouter = Scouter.NONE,
        val matchData: MatchData = MatchData()
    )

    // Create the data store serializer
    object TabletSerializer : Serializer<TabletDataModel> {

        // Implementing default values for the data store
        override val defaultValue: TabletDataModel = TabletDataModel()

        // Implementing the ability to read from the stored data
        override suspend fun readFrom(input: InputStream): TabletDataModel {
            return try {
                Json.decodeFromString<TabletDataModel>(input.readBytes().decodeToString())
            } catch (_: SerializationException) {
                TabletDataModel()
            }
        }

        // Implementing the ability to write data to the file
        override suspend fun writeTo(t: TabletDataModel, output: OutputStream) {
            output.write(Json.encodeToString(t).encodeToByteArray())
        }

    }

    // Create the data store
    val Context.dataStore: DataStore<TabletDataModel> by dataStore(
        fileName = "tablet_data.json",
        serializer = TabletSerializer
    )

    /* ----------------------------------------------------------------------------------------- */
    // [METHODS]
    /* ----------------------------------------------------------------------------------------- */

    fun getScouter(): Flow<Scouter> = context.dataStore.data.map { it.scouting }
    fun setScouter(value: Scouter) {
        coroutine.launch { context.dataStore.updateData { it.copy(scouting = value) } }
    }
    fun withScouter(lambda: suspend (Scouter) -> Unit) {
        coroutine.launch { getScouter().collect { lambda(it) } }
    }

    fun getMatch(): Flow<MatchData> = context.dataStore.data.map { it.matchData }
    fun setMatch(value: MatchData) {
        coroutine.launch { context.dataStore.updateData { it.copy(matchData = value) } }
    }
    fun withMatch(lambda: suspend (MatchData) -> Unit) {
        coroutine.launch { getMatch().collect { lambda(it) } }
    }

}