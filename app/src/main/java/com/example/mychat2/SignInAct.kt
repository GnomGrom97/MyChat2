package com.example.mychat2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mychat2.databinding.ActivityMainBinding
import com.example.mychat2.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.BuildConfig
import com.google.firebase.auth.ktx.auth
//Language version: 1.7 -> 1.8    для устранения ошибки с auth
// API version: 1.7 -> 1.8
/**проблема в синхронизации
 убрал строчку implementation 'com.google.android.gms:play-services:12.0.1'
API обмена сообщениями в приложениях Firebase ранее не использовался в проекте Включите его, посетив
https://console.cloud.google.com/apis/library/firebaseinappmessaging.googleapis.com?project=mychat-1984a
приложение работает но не запускает  вход в гугл, утром исправь
разных версий грандл и проекта**/
//робит и не  робит
// для запуска версию classpath 'com.google.gms:google-services:4.3.15
//в целом из авторизации и з goggle signIn


class SignInAct : AppCompatActivity() {
    lateinit var launcher:ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //В методе onCreate вашего действия входа в систему получите общий экземпляр объекта FirebaseAuth:

        //Оба возвращают один и тот же экземпляр. Последнее является свойством расширения ,
        // которое является предпочтительным типом создания экземпляров в Котлине.

        //елси currentUser выдает пользователя то успешно зарегистрировались если нет то нет
        //auth.currentUser
        //регистрация лаунчера
        launcher =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            //с помощью функции достается наш аккаунт и записывается в переменную таск
            val  task= GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                //достаем из аккаунта наш таск(но отслеживаем есть ли ошибки
                val account =task.getResult(ApiException::class.java)
                if(account != null){
                    //если нет ошибок, то подключаем гуг аккаунт к firebase: получаем токен из  аккаунта
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            }catch (e: ApiException){
                Log.d("My log", "Api exception")
            }
        }
        //создание слушателя нажатий
        binding.bSignIn.setOnClickListener {
            //нужно

        }
    }
    //функция запуска регистрации гугл аккаунта
    private fun getClient(): GoogleSignInClient{
        val gso=GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("420272150873-92ppv3pl173cbrhca17tsnghgmh0lbhv.apps.googleusercontent.com")
            .requestEmail()
            .build()
        //получение токена для регистрации на firebase
        return GoogleSignIn.getClient(this,gso)
    }
    private  fun signInWithGoogle(){
        //запуск GoogleSignIn с помощью лаучера
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)

    }
    private fun firebaseAuthWithGoogle(idToken:String){
        val credential= GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Log.d("My log", "Google signIn done")
            }else{
                Log.d("My log", "Google signIn error")

            }

        }
    }
}