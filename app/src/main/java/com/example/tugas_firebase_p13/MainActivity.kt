package com.example.tugas_firebase_p13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_firebase_p13.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var pengaduanAdapter: PengaduanAdapter

    // Firebase
    private val firebase = FirebaseFirestore.getInstance()
    private val pengaduanCollectionRef = firebase.collection("pengaduan")

    companion object {
        const val FORM_ACTIVITY_REQUEST_CODE = 1
        const val DETAIL_ACTIVITY_REQUEST_CODE = 2
        const val EXTRA_UPDATE_ID = "extra_update_id"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_JUDUL = "extra_judul"
        const val EXTRA_ISI = "extra_isi"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, FormActivity::class.java)
            startActivityForResult(intent, FORM_ACTIVITY_REQUEST_CODE)
        }

        loadPengaduanData()
    }

    private fun setupRecyclerView() {
        pengaduanAdapter = PengaduanAdapter(emptyList(), object : PengaduanAdapter.OnEditClickListener {
            override fun onEditClick(pengaduan: Pengaduan) {
                val intent = Intent(this@MainActivity, FormActivity::class.java).apply {
                    putExtra(EXTRA_UPDATE_ID, pengaduan.id)
                    putExtra(EXTRA_NAME, pengaduan.nama)
                    putExtra(EXTRA_JUDUL, pengaduan.judul)
                    putExtra(EXTRA_ISI, pengaduan.isi)
                }
                startActivityForResult(intent, DETAIL_ACTIVITY_REQUEST_CODE)
            }

            override fun onDeleteClick(pengaduan: Pengaduan) {
                deletePengaduan(pengaduan)
            }

            override fun onDetailClick(pengaduan: Pengaduan) {
                val intent = Intent(this@MainActivity, DetailNotes::class.java).apply {
                    putExtra(EXTRA_UPDATE_ID, pengaduan.id)
                    putExtra(EXTRA_NAME, pengaduan.nama)
                    putExtra(EXTRA_JUDUL, pengaduan.judul)
                    putExtra(EXTRA_ISI, pengaduan.isi)
                }
                startActivityForResult(intent, DETAIL_ACTIVITY_REQUEST_CODE)
            }
        })

        binding.rvAdu.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = pengaduanAdapter
        }
    }

    private fun loadPengaduanData() {
        pengaduanCollectionRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle the error
                return@addSnapshotListener
            }

            val pengaduanList = mutableListOf<Pengaduan>()
            for (document in snapshot!!) {
                val pengaduan = document.toObject(Pengaduan::class.java)
                pengaduanList.add(pengaduan)
            }
            pengaduanAdapter.updateData(pengaduanList)
        }
    }

    private fun deletePengaduan(pengaduan: Pengaduan) {
        pengaduanCollectionRef.document(pengaduan.id)
            .delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FORM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
        } else if (requestCode == DETAIL_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
        }
    }
}
