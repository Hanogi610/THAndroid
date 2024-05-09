package com.example.thandroid.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work")
data class Work(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val name : String,
    val content : String,
    val date : Long,
    val status : String,
    val coop : Boolean
)
