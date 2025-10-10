package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.application.ProfileActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        val name = findViewById<EditText>(R.id.etName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val rgRole = findViewById<RadioGroup>(R.id.rgRole)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            val nameTxt = name.text.toString()
            val phoneTxt = phone.text.toString()
            val emailTxt = email.text.toString()
            val passTxt = password.text.toString()
            val role = when (rgRole.checkedRadioButtonId) {
                R.id.rbBuyer -> "Buyer"
                R.id.rbSeller -> "Seller"
                else -> ""
            }

            if (emailTxt.isEmpty() || passTxt.isEmpty() || nameTxt.isEmpty() || phoneTxt.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(emailTxt, passTxt)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid!!
                        val user = mapOf(
                            "name" to nameTxt,
                            "phone" to phoneTxt,
                            "email" to emailTxt,
                            "role" to role
                        )
                        dbRef.child(uid).setValue(user)
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
