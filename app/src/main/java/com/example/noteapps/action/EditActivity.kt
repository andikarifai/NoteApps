package com.example.noteapps.action

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteapps.databinding.ActivityEditBinding
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    var noteDB : NoteDataBase ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDB = NoteDataBase.getInstance(this)

        var getData = intent.getSerializableExtra("data_edit") as NoteData

        binding.idNote.setText(getData.id.toString())
        binding.editTitle.setText(getData.title)
        binding.editContent.setText(getData.content)
        binding.editDate.setText(getData.date)

        binding.btnEditNote.setOnClickListener{
            editNote()
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        // Tampilkan tombol kembali di toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Tambahkan listener ke tombol kembali di toolbar
        toolbar.setNavigationOnClickListener {
            // Tutup aktivitas saat tombol kembali ditekan
            finish()
        }

    }
    fun editNote(){
        val idNote = binding.idNote.text.toString().toInt()
        val title = binding.editTitle.text.toString()
        val content = binding.editContent.text.toString()
        val date = binding.editDate.text.toString()

        GlobalScope.async {
            var edit = noteDB?.noteDao()?.updateNote(NoteData(idNote,title,content, date))
            runOnUiThread{
                Toast.makeText(this@EditActivity, "Data Berhasil diubah", Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

}