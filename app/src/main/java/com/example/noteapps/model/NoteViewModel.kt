package com.example.noteapps.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NoteViewModel(app : Application): AndroidViewModel(app){

    lateinit var allNote : MutableLiveData<List<NoteData>>

    init {
        allNote = MutableLiveData()
        getAllNote()
    }
    fun getAllNoteObservers(): MutableLiveData<List<NoteData>> {
        return allNote
    }

    private fun getAllNote() {
        GlobalScope.launch {
            val userDao = NoteDataBase.getInstance(getApplication())!!.noteDao()
            val list = userDao.getDataNote()
            allNote.postValue(list)
        }
    }
}