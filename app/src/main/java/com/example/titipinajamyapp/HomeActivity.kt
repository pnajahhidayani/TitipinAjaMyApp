package com.example.titipinajamyapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var db: AppDatabase
    var name="null"
    var email="null"
    var password=null
    var providerId ="null"
    var username=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                providerId = profile.providerId.toString()


                // Name, email address, and profile photo Url
                name = profile.displayName.toString()
                email = profile.email.toString()

            }
        }
        Log.d("nama", name)



        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.home_toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi PostAdapter
        postAdapter = PostAdapter()

        // Set adapter pada RecyclerView
        recyclerView.adapter = postAdapter

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "my-database")
            .build()


        // Ambil data posting dari database
        lifecycleScope.launch {
            val postings = db.postingDao().getAllPostings()
            postAdapter.setPostings(postings)
        }

    }
}