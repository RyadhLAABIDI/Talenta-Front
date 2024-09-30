package com.example.talenta
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var buttonInscription: Button
    private lateinit var buttonConnexion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        buttonInscription = findViewById(R.id.buttonInscription)
        buttonConnexion = findViewById(R.id.buttonConnexion)

        buttonInscription.setOnClickListener {
            // Rediriger vers l'interface d'inscription
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonConnexion.setOnClickListener {
            // Rediriger vers l'interface de connexion
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
