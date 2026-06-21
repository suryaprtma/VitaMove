package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entitas User untuk menyimpan profil pengguna secara profesional.
 */
@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    val uid: String,
    val fullName: String,
    val email: String,
    val address: String = "",
    val phone: String = "",
    val photoUrl: String? = null
)
