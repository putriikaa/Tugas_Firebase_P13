package com.example.tugas_firebase_p13

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class DetailNotes : AppCompatActivity() {

    private lateinit var namaTextView: TextView
    private lateinit var judulTextView: TextView
    private lateinit var aduanTextView: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var backto: ImageView  // assuming backto is an ImageView

    // Firebase
    private val firebase = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firebase.collection("pengaduan")

    private lateinit var updateId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_notes)

        namaTextView = findViewById(R.id.nama)
        judulTextView = findViewById(R.id.judul)
        aduanTextView = findViewById(R.id.aduan)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        backto = findViewById(R.id.backto)

        val intent = intent
        val nama = intent.getStringExtra(MainActivity.EXTRA_NAME)
        val judul = intent.getStringExtra(MainActivity.EXTRA_JUDUL)
        val aduan = intent.getStringExtra(MainActivity.EXTRA_ISI)
        updateId = intent.getStringExtra(MainActivity.EXTRA_UPDATE_ID) ?: ""

        namaTextView.text = nama
        judulTextView.text = judul
        aduanTextView.text = aduan

        btnEdit.setOnClickListener {
            // Pindah ke FormActivity untuk mengedit data
            val editIntent = Intent(this, FormActivity::class.java)
            editIntent.putExtra(FormActivity.EXTRA_UPDATE_ID, updateId)
            editIntent.putExtra(FormActivity.EXTRA_NAME, nama)
            editIntent.putExtra(FormActivity.EXTRA_JUDUL, judul)
            editIntent.putExtra(FormActivity.EXTRA_ISI, aduan)
            startActivity(editIntent)
        }

        btnDelete.setOnClickListener {
            // Hapus data dari Firebase
            deleteData()
        }

        backto.setOnClickListener {
            val intent = Intent(this@DetailNotes, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteData() {
        if (updateId.isNotEmpty()) {
            pengaduanCollectionRef.document(updateId).delete()
                .addOnSuccessListener {
                    // Data berhasil dihapus, kembali ke MainActivity
                    setResultAndFinish()
                }
                .addOnFailureListener {
                    // Gagal menghapus data, tampilkan pesan atau log jika diperlukan
                }
        }
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent().apply {
            setResult(RESULT_OK, this)
        }
        finish()
        val mainIntent = Intent(this@DetailNotes, MainActivity::class.java)
        startActivity(mainIntent)
    }
}