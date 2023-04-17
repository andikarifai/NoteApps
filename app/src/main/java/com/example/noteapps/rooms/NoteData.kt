package com.example.noteapps.rooms

import androidx.room.*
import java.io.Serializable

@Entity
data class NoteData(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var title : String,
    var content : String,
    var date : String,
): Serializable
