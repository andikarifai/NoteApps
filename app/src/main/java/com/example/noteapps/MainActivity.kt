package com.example.noteapps

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapps.action.AddActivity
import com.example.noteapps.adapter.NoteAdapter
import com.example.noteapps.databinding.ActivityMainBinding
import com.example.noteapps.model.NoteViewModel
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var NoteDB : NoteDataBase? = null
    lateinit var adapterNote : NoteAdapter
    lateinit var noteViewModel : NoteViewModel
    private lateinit var prefs: SharedPreferences


    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val SORT_BY_KEY = "sort_by"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NoteDB = NoteDataBase.getInstance(this)

        noteVM()
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        noteViewModel.getAllNoteObservers().observe(this, Observer {
            adapterNote.setData(it as ArrayList<NoteData>)
        })

        binding.btnAdd.setOnClickListener{
            startActivity(Intent(this, AddActivity::class.java))
        }
        // init prefs
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getAllNote() {
        GlobalScope.launch {
            val data = NoteDB?.noteDao()?.getDataNote()
            runOnUiThread{
                adapterNote = NoteAdapter(data!!)
                binding.rvListNote.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.rvListNote.adapter = adapterNote
            }
        }
    }
    fun noteVM(){
        adapterNote = NoteAdapter(ArrayList())
        binding.rvListNote.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvListNote.adapter = adapterNote
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            var data = NoteDB?.noteDao()?.getDataNote()
            runOnUiThread {
                adapterNote = NoteAdapter(data!!)
                binding.rvListNote.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                binding.rvListNote.adapter = adapterNote
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_title_asc -> {
                adapterNote.sortByTitleAsc()
                saveSortPreference("title_asc")
                true
            }
            R.id.sort_title_desc -> {
                adapterNote.sortByTitleDesc()
                saveSortPreference("title_desc")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveSortPreference(sortBy: String) {
        val editor = prefs.edit()
        editor.putString(SORT_BY_KEY, sortBy)
        editor.apply()

        // check if save was successful
        val savedSortBy = prefs.getString(SORT_BY_KEY, null)
        if (savedSortBy != sortBy) {
            Toast.makeText(this, "Gagal Mengurutkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Berhasil Mengurutkan", Toast.LENGTH_SHORT).show()
        }
    }
}