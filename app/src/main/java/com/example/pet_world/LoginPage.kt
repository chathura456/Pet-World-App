package com.example.pet_world

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class LoginPage: AppCompatActivity() {

    private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val btnsubmit=findViewById<Button>(R.id.btn_submit)
        val signupbtn=findViewById<TextView>(R.id.switchsignup)

        firebaseAuth = FirebaseAuth.getInstance()

        btnsubmit.setOnClickListener {
            if (email.text.toString().trim().isNotEmpty()&&password.text.toString().trim().isNotEmpty())
                firebaseAuth.signInWithEmailAndPassword(email.text.toString().trim(),password.text.toString().trim()).addOnCompleteListener{
                    if(it.isSuccessful){
                        val intent= Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            else{
                Toast.makeText(this,"email and password required",Toast.LENGTH_SHORT).show()
            }



        }

        signupbtn.setOnClickListener{
            val intent= Intent(this,SignUpPage::class.java)
            startActivity(intent)
        }

    }

    /*
    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser!= null){
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

*/

}