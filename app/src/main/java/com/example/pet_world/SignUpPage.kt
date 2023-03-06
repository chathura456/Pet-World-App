package com.example.pet_world

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignUpPage : AppCompatActivity() {


    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        firebaseAuth = FirebaseAuth.getInstance()


        val btn_submit = findViewById<Button>(R.id.btn_submit)
        val username = findViewById<EditText>(R.id.user_name)
        val email = findViewById<EditText>(R.id.email)
        val phone = findViewById<EditText>(R.id.phone)
        val password = findViewById<EditText>(R.id.password)
        val warning = findViewById<TextView>(R.id.txterror)




        btn_submit.setOnClickListener {



            if (username.text.toString().trim().isNotEmpty() && email.text.toString().trim()
                    .isNotEmpty() && phone.text.toString().trim().isNotEmpty() && password.text.toString().trim().isNotEmpty()
            ) {
                val email1=email.text.toString().trim()
                val pass= password.text.toString()


                    firebaseAuth.createUserWithEmailAndPassword(email1, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this@SignUpPage, OTPVerifyActivity::class.java)
                                startActivity(intent)
                            }else{
                                try {
                                    throw it.exception!!
                                }catch ( e:FirebaseAuthWeakPasswordException ){
                                    Toast.makeText(this,"password is not strong",Toast.LENGTH_SHORT).show()
                                }catch (e:FirebaseAuthUserCollisionException){
                                    Toast.makeText(this,"user already exist",Toast.LENGTH_SHORT).show()
                                }catch (e:FirebaseAuthInvalidCredentialsException){
                                    Toast.makeText(this,"user already exist",Toast.LENGTH_SHORT).show()
                                }



                            }
                        }



            } else {
                warning.text = "Input required"
            }
        }



    }


}