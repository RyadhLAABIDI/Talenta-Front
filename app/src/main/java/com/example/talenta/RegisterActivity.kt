package com.example.talenta

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.talenta.api.BackendService
import com.example.talenta.database.User
import com.example.talenta.database.UserDao
import com.example.talenta.database.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private lateinit var backendService: BackendService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText = findViewById<EditText>(R.id.edtEmail)
        val passwordEditText = findViewById<EditText>(R.id.edtPassword)
        val roleSpinner = findViewById<Spinner>(R.id.roleSpinner)
        val registerButton = findViewById<Button>(R.id.button_inscription)
        val loginButton = findViewById<Button>(R.id.button_login)

        val userDatabase = UserDatabase.getDatabase(this)
        userDao = userDatabase.userDao()

        // Initialisation de BackendService
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5002/") // Assurez-vous que l'URL de base est correcte
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        backendService = retrofit.create(BackendService::class.java)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleSpinner.selectedItem.toString().lowercase()

            val user = User(email = email, password = password, role = role)

            registerUser(user)
        }

        loginButton.setOnClickListener {
            // Redirection vers l'interface com.example.talenta.com.example.talenta.com.example.talenta.com.example.talenta.LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(user: User) {
        GlobalScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    backendService.registerUser(user)
                }
                println(response)
                if (response.isSuccessful) {
                    withContext(Dispatchers.IO) {
                        userDao.insert(user)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Inscription réussie pour l'utilisateur ${user.email}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Ajouter des logs pour afficher les informations de l'utilisateur
                        println("Utilisateur inscrit avec succès:")
                        println("Email: ${user.email}")
                        println("Password: ${user.password}")
                        println("Role: ${user.role}")


                        // Rediriger l'utilisateur vers l'interface de connexion
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Erreur",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("Erreur: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Erreur lors de l'inscription: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}