package com.example.talenta

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.talenta.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainTalentActivity : AppCompatActivity() {

    private lateinit var btnProfil: Button
    private lateinit var btnDeconnexion: Button
    private lateinit var btnSupprimerCompte: Button

    private var userId: String = ""
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_talent)

        userId = intent.getStringExtra("userId") ?: ""

        btnProfil = findViewById(R.id.btnProfil)
        btnDeconnexion = findViewById(R.id.btnDeconnexion)
        btnSupprimerCompte = findViewById(R.id.btnSupprimerCompte)

        btnProfil.setOnClickListener {
            val intent = Intent(this, ProfilUserActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        btnDeconnexion.setOnClickListener {
            logoutUser()
        }

        btnSupprimerCompte.setOnClickListener {
            deleteTalent()
        }
    }

    private fun logoutUser() {
        apiService.logout().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Déconnexion réussie, redirigez vers l'écran de connexion
                    val intent = Intent(this@MainTalentActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Facultatif : ferme l'activité actuelle après la déconnexion
                } else {
                    // Gérer les erreurs de déconnexion ici
                    Toast.makeText(
                        this@MainTalentActivity,
                        "Erreur lors de la déconnexion. Veuillez réessayer. Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Gérer les erreurs réseau ici
                Toast.makeText(
                    this@MainTalentActivity,
                    "Erreur réseau lors de la déconnexion. Veuillez réessayer.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun deleteTalent() {
        apiService.deleteTalent(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Suppression du compte réussie, redirigez vers l'écran de connexion
                    val intent = Intent(this@MainTalentActivity, RegisterActivity::class.java)
                    startActivity(intent)
                    finish() // Facultatif : ferme l'activité actuelle après la suppression du compte
                } else {
                    // Gérer les erreurs de suppression du compte ici
                    Toast.makeText(
                        this@MainTalentActivity,
                        "Erreur lors de la suppression du compte. Veuillez réessayer. Code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Gérer les erreurs réseau ici
                Toast.makeText(
                    this@MainTalentActivity,
                    "Erreur réseau lors de la suppression du compte. Veuillez réessayer.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
