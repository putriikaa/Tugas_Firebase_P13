package com.example.tugas_firebase_p13

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_firebase_p13.databinding.ListNoteBinding

class PengaduanAdapter(
    private var listNote: List<Pengaduan>,
    private val onEditClickListener: OnEditClickListener
) : RecyclerView.Adapter<PengaduanAdapter.ItemPengaduanViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(pengaduan: Pengaduan)
        fun onDeleteClick(pengaduan: Pengaduan)
        fun onDetailClick(pengaduan: Pengaduan)
    }

    inner class ItemPengaduanViewHolder(private val binding: ListNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onDetailClick(listNote[position])
                }
            }

            binding.iconEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onEditClick(listNote[position])
                }
            }

            binding.iconDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener.onDeleteClick(listNote[position])
                }
            }
        }

        fun bind(pengaduan: Pengaduan) {
            with(binding) {
                textNama.text = pengaduan.nama
                textJudul.text = pengaduan.judul
                textIsi.text = pengaduan.isi
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPengaduanViewHolder {
        val binding = ListNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPengaduanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemPengaduanViewHolder, position: Int) {
        holder.bind(listNote[position])
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    fun updateData(newList: List<Pengaduan>) {
        listNote = newList
        notifyDataSetChanged()
    }
}
