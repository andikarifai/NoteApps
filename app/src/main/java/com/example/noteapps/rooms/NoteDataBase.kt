package com.example.noteapps.rooms

import android.content.Context
import androidx.room.*

@Database(entities = [NoteData::class], version = 1, exportSchema = false)

abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao() : NoteDao

    companion object{

        private var INSTANCE : NoteDataBase? = null

        fun getInstance(context : Context):NoteDataBase? {
            if (INSTANCE == null){
                synchronized(NoteDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoteDataBase::class.java,"Note.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}