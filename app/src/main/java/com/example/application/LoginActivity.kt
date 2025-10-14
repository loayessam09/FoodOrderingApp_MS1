package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        val emailField = findViewById<EditText>(R.id.etEmail)
        val passwordField = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToSignUp = findViewById<Button>(R.id.btnGoToSignUp)
        val btnProfile = findViewById<Button>(R.id.btnProfile)
        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)

        var loggedInUserName: String? = null

        btnLogin.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        database.child(userId).get().addOnSuccessListener { snapshot ->
                            loggedInUserName = snapshot.child("name").value.toString()

                            // Show welcome and profile button
                            tvWelcome.text = "Welcome, $loggedInUserName!"
                            tvWelcome.visibility = TextView.VISIBLE
                            btnProfile.visibility = Button.VISIBLE

                            // Hide login form
                            emailField.visibility = EditText.GONE
                            passwordField.visibility = EditText.GONE
                            btnLogin.visibility = Button.GONE
                            btnGoToSignUp.visibility = Button.GONE
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnGoToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("userName", loggedInUserName)
            startActivity(intent)
        }
    }
}
