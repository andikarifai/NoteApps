package com.example.noteapps.action

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapps.MainActivity
import com.example.noteapps.databinding.ActivityDetailBinding
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    var NoteDB : NoteDataBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        // Tampilkan tombol kembali di toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val getDetail = intent.getSerializableExtra("detail") as NoteData

        binding.tvTitle.text = getDetail.title
        binding.tvContent.text = getDetail.content
        binding.tvDate.text = getDetail.date
        binding.btnEditDetail.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("data_edit", getDetail)
            startActivity(intent)
            finish()
        }
        binding.btnDeleteDetail.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
                .setPositiveButton("Ya") { _, _ ->
                    // aksi saat pengguna menekan tombol Ya
                    NoteDB = NoteDataBase.getInstance(this)
                    GlobalScope.async {
                        val del = NoteDB?.noteDao()?.deleteNote(getDetail)
                        runOnUiThread {
                            Toast.makeText(this@DetailActivity, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    // aksi saat pengguna menekan tombol Tidak
                    dialog.dismiss()
                }
                .show()
        }



    }
}