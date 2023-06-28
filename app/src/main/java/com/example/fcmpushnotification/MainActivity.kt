package com.example.fcmpushnotification

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging

class MainActivity : AppCompatActivity() {

    private lateinit var emailUser: EditText
    private lateinit var passwordUser: EditText

    private val googleSignIn = 100

    companion object {
        private const val TAG = "MainActivity"
    }

    @SuppressLint("AppCompatMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PlanDeBackend)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        subscribeTopics()
        notification()
        // Ocultar la Action Bar
        supportActionBar?.hide()
        actionBar?.hide()
        setup()
    }

    private fun notification(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            println("Este es el token : ${it.result}")
        }
    }

    private fun subscribeTopics() {
        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]
    }
    private fun setup(){
    }


    fun loginGoogle(view: View){
        //Configuración

        val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this,googleConfig)
        googleClient.signOut()

        startActivityForResult(googleClient.signInIntent,googleSignIn)
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error en la autenticación de usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertPerson(title: String,message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String , provider: ProviderType ){
        val homeIntent = Intent(this, NavigationDrawerActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider",provider.name)
        }
        Log.d("Email final",email)
        Log.d("Provider final", provider.toString())

        startActivity(homeIntent)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == googleSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val emailValidationBanco = account.email.toString()
                            val emailValidationBancoT = emailValidationBanco.indexOf("@")
                            if (emailValidationBanco.substring(emailValidationBancoT).uppercase() == "@BBVA.COM" ){
                                showHome(account.email ?: "", ProviderType.GOOGLE)
                            }else
                            {
                                val title = "Error de autenticación"
                                val message = "Correo ${account.email.toString()} ,no valido "
                                showAlertPerson(title,message)
                            }

                        } else {
                            showAlert()
                        }
                    }

                }
            }catch (e: ApiException){
                showAlert()
            }


        }
    }
}