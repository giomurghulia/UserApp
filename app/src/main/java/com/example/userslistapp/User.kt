package com.example.userslistapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    var name: String,
    var lastName: String,
    var email: String
):Parcelable