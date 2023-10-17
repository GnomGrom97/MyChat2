package com.example.mychat2

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mychat2.databinding.ActivityMainBinding
import com.example.mychat2.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
/** обновляю версию до 34  с 33*/
/**перешел на 11 java in project structure*/
/**изменил версию котлин в уровне проекта на 1.7.1, была больше 1.7.20 кажется**/
//запустил синхрон грандла


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //подключение к бд
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar()
        auth=Firebase.auth
        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener {
            //при нажатии на кнопку будет записываться в бд
            //данные из текстового поля превратили в стринг
            myRef.setValue(binding.edMessage.text.toString())
        }
        //запуск слушателя нажатий на пити mRef
        onChangeListener(myRef)
    }
    //выведение изменений на экран
    private  fun onChangeListener(dRef:DatabaseReference){
        //слушатель постоянных нажатий
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               //при изменении данных
                binding.apply {
                    rcView.append("\n")//переход на строку ниже
                    //полученное значение ппревращает в строку и передаем
                    rcView.append("Petr: ${snapshot.value.toString()}")

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
}
        /** Функция подключения верхней панели**/
        private  fun setActionBar(){
            val ab=supportActionBar
            Thread{
                val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
                val dIcon = BitmapDrawable(resources, bMap)
                runOnUiThread {
                    //запуск картинки проекта
                    ab?.setDisplayHomeAsUpEnabled(true)
                    ab?.setHomeAsUpIndicator(dIcon)
                    ab?.title = auth.currentUser?.displayName
                }
            }.start()

        }
}