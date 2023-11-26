package com.example.tugas_firebase_p13

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_firebase_p13.databinding.ActivityFormBinding
import com.google.firebase.firestore.FirebaseFirestore

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    companion object {
        const val EXTRA_UPDATE_ID = "extra_update_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_JUDUL = "extra_judul"
        const val EXTRA_ISI = "extra_isi"
    }

    // Firebase
    private val firebase = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firebase.collection("pengaduan")

    private var updateId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateId = intent.getStringExtra(EXTRA_UPDATE_ID) ?: ""

        if (updateId.isNotEmpty()) {
            // If ID is not empty, it's an update mode
            val nama = intent.getStringExtra(EXTRA_NAME) ?: ""
            val judul = intent.getStringExtra(EXTRA_JUDUL) ?: ""
            val isi = intent.getStringExtra(EXTRA_ISI) ?: ""

            with(binding) {
                txtNama.setText(nama)
                txtTitle.setText(judul)
                txtIsi.setText(isi)
            }
        }

        with(binding) {
            btnAdd.setOnClickListener {
                if (txtNama.text.isNotEmpty() && txtTitle.text.isNotEmpty() && txtIsi.text.isNotEmpty()) {
                    val nama = txtNama.text.toString()
                    val judul = txtTitle.text.toString()
                    val isi = txtIsi.text.toString()
                    val newPengaduan = Pengaduan(
                        nama = nama,
                        judul = judul,
                        isi = isi
                    )
                    addData(newPengaduan)
                    resetForm()
                    setResultAndFinish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please fill the form correctly",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            btnUpdate.setOnClickListener {
                val nama = txtNama.text.toString()
                val judul = txtTitle.text.toString()
                val isi = txtIsi.text.toString()
                val pengaduanUpdate = Pengaduan(
                    nama = nama,
                    judul = judul,
                    isi = isi
                )
                updateData(pengaduanUpdate)
                updateId = ""
                setResultAndFinish()
            }

            back.setOnClickListener {
                finish()
            }
        }
    }

    private fun addData(pengaduan: Pengaduan) {
        pengaduanCollectionRef.add(pengaduan)
            .addOnSuccessListener { docRef ->
                val createPengaduanId = docRef.id
                pengaduan.id = createPengaduanId
                docRef.set(pengaduan)
                    .addOnFailureListener {
                        Log.d("FormActivity", "Error updating data ID: ", it)
                    }
                resetForm()
            }
            .addOnFailureListener {
                Log.d("FormActivity", "Error adding data: ", it)
            }
    }

    private fun resetForm() {
        with(binding) {
            txtNama.setText("")
            txtTitle.setText("")
            txtIsi.setText("")
        }
    }

    private fun updateData(pengaduan: Pengaduan) {
        pengaduan.id = updateId
        pengaduanCollectionRef.document(updateId).set(pengaduan)
            .addOnFailureListener {
                Log.d("FormActivity", "Error updating data:", it)
            }
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent().apply {
            setResult(Activity.RESULT_OK, this)
        }
        finish()
    }
}
