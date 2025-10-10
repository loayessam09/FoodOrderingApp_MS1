
package com.example.application // It's good practice to declare the package

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

        val name = findViewById<EditText>(R.id.etName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)

        // Fetch data
        dbRef.get().addOnSuccessListener { dataSnapshot ->
            // Use dataSnapshot to avoid ambiguity and ensure safety
            if (dataSnapshot.exists()) {
                val currentName = dataSnapshot.child("name").getValue(String::class.java)
                val currentPhone = dataSnapshot.child("phone").getValue(String::class.java)
                name.setText(currentName)
                phone.setText(currentPhone)
            }
        }.addOnFailureListener {
            // It's good practice to handle potential errors
            Toast.makeText(this, "Failed to load profile data.", Toast.LENGTH_SHORT).show()
        }

        btnUpdate.setOnClickListener {
            val updatedName = name.text.toString()
            val updatedPhone = phone.text.toString()

            if (updatedName.isNotEmpty() && updatedPhone.isNotEmpty()) {
                val updates = mapOf(
                    "name" to updatedName,
                    "phone" to updatedPhone
                )
                dbRef.updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Name and phone cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
