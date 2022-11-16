package com.bignerdranch.android.criminalintent

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Crime(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
    //var requiresPolice : Boolean = false
)