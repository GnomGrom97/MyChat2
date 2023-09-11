package com.example.mychat2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //подключение к бд
        setContentView(R.layout.activity_main)
        val database = Firebase.database
        //записываем в бд message:hello...
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
    }
}