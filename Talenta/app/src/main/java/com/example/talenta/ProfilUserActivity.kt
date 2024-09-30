package com.example.talenta

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.talenta.api.ApiService
import com.example.talenta.api.UpdateUserRequest
import com.example.talenta.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfilUserActivity : AppCompatActivity() {

    private lateinit var tvEmail: TextView
    private lateinit var tvPassword: TextView
    private lateinit var etNewEmail: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnRetour: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil_user)

        // Initialiser les TextViews
        tvEmail = findViewById(R.id.tvEmail)
        tvPassword = findViewById(R.id.tvPassword)

        // Initialiser les EditTexts pour les nouvelles informations
        etNewEmail = findViewById(R.id.etEmail)
        etNewPassword = findViewById(R.id.etPassword)

        // Bouton de mise à jour
        btnUpdate = findViewById(R.id.btnModifier)

        btnRetour = findViewById(R.id.btnRetour)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Récupérer l'ID de l'utilisateur depuis l'intent
        val userId = intent.getStringExtra("userId")

        if (userId != null) {
            Log.d("ProfilUserActivity", "ID de l'utilisateur récupéré avec succès: $userId")

            val call = apiService.getUser(userId)

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        val user = response.body()?.user

                        if (user != null) {
                            // Utiliser les TextViews pour afficher les informations de l'utilisateur
                            tvEmail.text = "Email: ${user.email}"

                            // Utiliser PasswordTransformationMethod pour masquer le mot de passe avec des points
                            tvPassword.text = "Mot de passe: ********"
                            tvPassword.transformationMethod = PasswordTransformationMethod.getInstance()

                            // Mettre à jour les EditTexts avec les informations actuelles
                            etNewEmail.setText(user.email)
                            etNewPassword.setText("") // Laissez le champ vide pour des raisons de sécurité

                            // Bouton de mise à jour
                            btnUpdate.setOnClickListener {
                                // Appel de la fonction de mise à jour ici
                                updateUserInfo(userId, etNewEmail.text.toString(), etNewPassword.text.toString())
                            }
                        } else {
                            Log.d("ProfilUserActivity", "Aucune information utilisateur trouvée")
                        }
                    } else {
                        Log.d("ProfilUserActivity", "Erreur lors de la récupération des informations de l'utilisateur. Code d'erreur: ${response.code()}")
                        // Gérer les erreurs de l'API ici
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("ProfilUserActivity", "Erreur réseau lors de la récupération des informations de l'utilisateur")
                    // Gérer les erreurs de réseau ici
                }
            })
        } else {
            Log.d("ProfilUserActivity", "ID de l'utilisateur non trouvé dans l'intent")
        }
        btnRetour.setOnClickListener {
            // Rediriger vers MainUserActivity
            val intent = Intent(this@ProfilUserActivity, MainUserActivity::class.java)
            startActivity(intent)
            finish() // Facultatif : ferme l'activité actuelle
        }
    }

    private fun updateUserInfo(userId: String, newEmail: String, newPassword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5002/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Créer un objet UpdateUserRequest avec les nouvelles informations
        val updateData = UpdateUserRequest(newEmail, newPassword)
        val updateCall = apiService.updateUser(userId, updateData)

        // Appel de l'API pour mettre à jour les informations de l'utilisateur
        updateCall.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("ProfilUserActivity", "Mise à jour réussie")

                    // Mettre à jour l'interface utilisateur avec les nouvelles informations
                    tvEmail.text = "Email: $newEmail"
                    tvPassword.text = "Mot de passe: ********"

                    Toast.makeText(this@ProfilUserActivity, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("ProfilUserActivity", "Erreur lors de la mise à jour. Code d'erreur: ${response.code()}")
                    Toast.makeText(this@ProfilUserActivity, "Erreur lors de la mise à jour. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
                    // Gérez les erreurs de l'API ici
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("ProfilUserActivity", "Erreur réseau lors de la mise à jour")
                Toast.makeText(this@ProfilUserActivity, "Erreur réseau lors de la mise à jour. Veuillez réessayer.", Toast.LENGTH_SHORT).show()
                // Gérez les erreurs de réseau ici
            }
        })
    }
}
