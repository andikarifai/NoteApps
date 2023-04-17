package com.example.noteapps.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapps.MainActivity
import com.example.noteapps.action.DetailActivity
import com.example.noteapps.action.EditActivity
import com.example.noteapps.databinding.ItemNoteBinding
import com.example.noteapps.rooms.NoteData
import com.example.noteapps.rooms.NoteDataBase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class NoteAdapter(var list: List<NoteData>): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    var NoteDB : NoteDataBase? = null

    class ViewHolder(var binding: ItemNoteBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvId.text = list[position].id.toString()
        holder.binding.tvTitle.text = list[position].title
        holder.binding.tvContent.text = list[position].content
        holder.binding.tvTime.text = list[position].date
        holder.binding.btnEditNote.setOnClickListener{
            val move = Intent(it.context, EditActivity::class.java)
            move.putExtra("data_edit", list[position])
            it.context.startActivity(move)
        }
        holder.binding.card.setOnClickListener{
            val detail = Intent(it.context, DetailActivity::class.java)
            detail.putExtra("detail", list[position])
            it.context.startActivity(detail)
        }
        holder.binding.btnDeleteNote.setOnClickListener{
            NoteDB = NoteDataBase.getInstance(it.context)

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Anda yakin ingin menghapus note ini?")

            builder.setPositiveButton("Ya"){dialog, which ->
                GlobalScope.async {
                    val del = NoteDB?.noteDao()?.deleteNote(list[position])
                    (holder.itemView.context as MainActivity).runOnUiThread{
                        (holder.itemView.context as MainActivity).getAllNote()
                    }
                }
            }

            builder.setNegativeButton("Batal"){dialog, which ->
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<NoteData>){
        this.list = list
    }
    fun sortByTitleAsc() {
        list = list.sortedBy { it.title.toLowerCase(Locale.getDefault()) }
        notifyDataSetChanged()
    }

    fun sortByTitleDesc() {
        list = list.sortedByDescending { it.title.toLowerCase(Locale.getDefault()) }
        notifyDataSetChanged()
    }
}