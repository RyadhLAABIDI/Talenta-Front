package com.example.talenta

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.talenta.api.LoginRequest
import com.example.talenta.api.LoginResponse
import com.example.talenta.api.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5002/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitService = retrofit.create(RetrofitService::class.java)

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)

        val registerTextView = findViewById<TextView>(R.id.textViewRegister)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val apiInterface: RetrofitService = retrofit.create(RetrofitService::class.java)
        val call: Call<LoginResponse> = apiInterface.loginUser(LoginRequest(email, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse: LoginResponse? = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        val userRole: String = loginResponse.userRole
                        val userId: String = loginResponse.userId

                        Log.d("LoginActivity", "Utilisateur connecté avec succès:")
                        Log.d("LoginActivity", "Email: $email")
                        Log.d("LoginActivity", "Role: $userRole")
                        Log.d("LoginActivity", "UserID: $userId")

                        saveUserId(userId)

                        when (userRole) {
                            "user" -> {
                                val intent = Intent(this@LoginActivity, MainUserActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                            }
                            "talent" -> {
                                val intent = Intent(this@LoginActivity, MainTalentActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Échec de la connexion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Erreur lors de la connexion",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserId(userId: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.apply()
    }

    private fun getUserId(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userId", null)
    }
}
