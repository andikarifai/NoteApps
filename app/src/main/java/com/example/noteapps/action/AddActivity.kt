package com.example.noteapps.action

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.noteapps.databinding.ActivityAddBinding
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class AddActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddBinding
    var noteDB : NoteDataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDB = NoteDataBase.getInstance(this)

        binding.fabSaveNote.setOnClickListener{
            addNote()
        }

    }
    fun addNote(){
        GlobalScope.async {
            val title = binding.editTextTitle.text.toString()
            val content = binding.editTextDesc.text.toString()
            val date = binding.tvDate.text.toString()

            noteDB!!.noteDao().insertNote(NoteData(0,title,content,date))
        }
    }
}