package com.example.rebuilt2026.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rebuilt2026.helper.Climb
import kotlinx.serialization.Serializable
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties

@Entity
@Serializable
data class MatchData(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // PreMatch
    val match: Int = 0,
    val team: Int = 0,

    // Auton
    val autonMoved: Boolean = false,
    val autonFuelPickup: Int = 0,
    val autonInactiveScore: Int = 0,
    val autonActiveScore: Int = 0,
    val autonPenalties: Int = 0,
    val autonClimb: Climb = Climb.NO_CLIMB,
    val autonQuality: Int = 0,

    // Teleop
    val teleopFuelPickup: Int = 0,
    val teleopInactiveScore: Int = 0,
    val teleopActiveScore: Int = 0,
    val teleopPenalties: Int = 0,

    // PostMatch
    val endClimb: Climb = Climb.NO_CLIMB,
    val matchQuality: Int = 0,

    // Notes
    val playedDefense: Boolean = false,
    val greatFuelPickup: Boolean = false,
    val didRobotDisable: Boolean = false,
    val notes: String = ""

) {

    /**
     * Fetch a map of each property value associated with its name.
     */
    fun getPropertyMap(): Map<String, Any> {
        return this::class.declaredMemberProperties.associate { property ->
            val value = property as? KProperty1<Any, Any>
            // We know that none of the MatchData properties are nullable so we can use !!
            property.name to value!!.get(this)
        }
    }

    companion object {

        /**
         * Companion function to fetch all property names.
         */
        fun getPropertyNames(): List<String> = MatchData::class.declaredMemberProperties.map { it.name }

    }

}
