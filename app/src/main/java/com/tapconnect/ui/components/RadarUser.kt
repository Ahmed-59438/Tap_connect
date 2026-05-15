package com.tapconnect.ui.components

/**
 * Represents a user appearing on the discovery radar.
 *
 * @param id Unique identifier for the user
 * @param name Display name of the user
 * @param distance Fraction of the radar radius (0.0 to 1.0)
 * @param angle Angle in degrees (0 to 360)
 */
data class RadarUser(
    val id: String,
    val name: String,
    val distance: Float,
    val angle: Float
)
