package com.example.noteapps.rooms

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note: NoteData)

    @Query("SELECT * FROM NoteData ORDER BY id DESC")
    fun getDataNote(): List<NoteData>


    @Delete
    fun deleteNote(note: NoteData)

    @Update
    fun updateNote(note: NoteData)
}