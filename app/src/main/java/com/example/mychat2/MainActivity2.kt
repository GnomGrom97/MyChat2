package com.example.mychat2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity2 : AppCompatActivity() {
    private lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        /**Подключение кнопки, а так же функции выхода**/
        btn=findViewById(R.id.button)
        btn.setOnClickListener {
            //выход и переход на предыдущуу вкладку
            FirebaseAuth.getInstance().signOut()
            val i= Intent(this,MainActivity::class.java)
            startActivity(i)
        }

    }
}