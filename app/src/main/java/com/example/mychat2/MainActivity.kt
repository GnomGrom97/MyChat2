package com.example.mychat2

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
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
/**изменил версию котлин на  1.9.10 кажется**/
//запустил синхрон грандла

//implementation 'com.google.firebase:firebase-auth-ktx:22.1.2'
//implementation 'androidx.core:core-ktx:1.7.1' c  последней на нее


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //подключение к бд
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        setUpActionBar()// запуск верхней панели
        val database = Firebase.database
        //создаем генерацию каждый раз новую,чтобы не перезаписывать сообщение
        val myRef = database.getReference("message")//функция содания узлов- в узле сообщений будут все сообщения
        binding.bSend.setOnClickListener {//слушатель нажатий
            //при нажатии на кнопку будет записываться в бд
            //данные из текстового поля превратили в стринг
            //генерирует специальный ключ, унмкальный путь
            myRef.child(myRef.push().key?:"blbbl").setValue(User(auth.currentUser?.displayName,binding.edMessage.text.toString()))//информация передается в узел
        }
        //запуск слушателя нажатий на пути mRef
        onChangeListener(myRef)//регистрируем слушатель
        initRcView()//инициализация адаптара ниже

    }
    private  fun initRcView()=with(binding){
    adapter  = UserAdapter()
    rcView.layoutManager=LinearLayoutManager(this@MainActivity)//для отображения данных по вертикали
    rcView.adapter = adapter //адаптер для заполнения rcView
    }
/** функция выведения меню на экран*/
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {//функция создания меню
    menuInflater.inflate(R.menu.main_menu,menu)//передает индефикатор
        return super.onCreateOptionsMenu(menu)
    }
    /** function log out**/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sign_out){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    //выведение изменений на экран
    private  fun onChangeListener(dRef:DatabaseReference){//создаем функцию и передаем в нее инфу из database.getReference
        //слушатель постоянных нажатий
        dRef.addValueEventListener(object : ValueEventListener{//addValueEventListener-прослугивает все на этом пути
            override fun onDataChange(snapshot: DataSnapshot) {//метод когда изменяются данные
            //передает значения в лист
            val list = ArrayList<User>()
               for (s in snapshot.children){
                   val user = s.getValue(User::class.java)
                   if (user != null)list.add(user)
               }
            adapter.submitList(list )
                }


            override fun onCancelled(error: DatabaseError) {

            }
        })
}
        /** Функция подключения верхней панели**/
        private  fun setUpActionBar(){
            val ab=supportActionBar
            Thread{
                //на второстепенном потоке загружаем картинку
                val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()//достаем картинку из гугл акка
                // ?- тк может быть null
                val dIcon = BitmapDrawable(resources, bMap)//превращаем bitmap в drawable
                runOnUiThread {//запуск на основном потоке
                    //запуск картинки проекта
                    ab?.setDisplayHomeAsUpEnabled(true)//активация Homebutton верхней полоски
                    ab?.setHomeAsUpIndicator(dIcon)//передаем суда картинку акка
                   //ab?.title = auth.currentUser?.displayName//передает название пользователя рядом с фото акка
                }
            }.start()
        }
}