package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity // <-- Add this import
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.application.BuyerHomeActivity
import com.example.application.SellerHomeActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        // Corrected the reference to get the "Users" path
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        val goToSignUp = findViewById<Button>(R.id.btnGoToSignUp)
        goToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val emailTxt = email.text.toString()
            val passTxt = password.text.toString()

            if (emailTxt.isEmpty() || passTxt.isEmpty()) {
                Toast.makeText(this, "Enter both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailTxt, passTxt)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            dbRef.child(uid).child("role").get().addOnSuccessListener { dataSnapshot ->
                                val role = dataSnapshot.value as? String
                                when (role) {
                                    "Buyer" -> startActivity(Intent(this, BuyerHomeActivity::class.java))
                                    "Seller" -> startActivity(Intent(this, SellerHomeActivity::class.java))
                                    else -> {
                                        // Handle cases where role is null or not "Buyer"/"Seller"
                                        Toast.makeText(this, "User role not found.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                finish()
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to retrieve user role.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
