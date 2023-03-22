package com.example.pet_world

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignUpPage : AppCompatActivity() {


    private  lateinit var firebaseAuth: FirebaseAuth


    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit  var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
        val repassword = findViewById<EditText>(R.id.repassword)
        val switchlog=findViewById<TextView>(R.id.switchlogin)





        btn_submit.setOnClickListener {


            //validate user inputs

            if (username.text.toString().trim().isNotEmpty() && email.text.toString().trim()
                    .isNotEmpty() && phone.text.toString().trim().isNotEmpty() && password.text.toString().trim().isNotEmpty() && repassword.text.toString().trim().isNotEmpty()
            ) {
                val uname=username.text.toString().trim()
                val email1=email.text.toString().trim()
                val pass= password.text.toString().trim()
                val rpass=repassword.text.toString().trim()

                if(pass==rpass) {


                    firebaseAuth.createUserWithEmailAndPassword(email1, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                //switch to phone number verification activity
                                val intent = Intent(this, OTPVerifyActivity::class.java)
                                    intent.putExtra("email", email1)
                                    intent.putExtra("username",uname)
                                    startActivity(intent)



                                //validating user inputs passing feedbacks
                            } else {
                                try {
                                    throw it.exception!!
                                } catch (e: FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(
                                        this,
                                        "password is not strong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: FirebaseAuthUserCollisionException) {
                                    Toast.makeText(this, "user already exist", Toast.LENGTH_SHORT)
                                        .show()
                                } catch (e: FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(this, "user already exist", Toast.LENGTH_SHORT)
                                        .show()
                                }catch (e: FirebaseNetworkException) {
                                    Toast.makeText(
                                        this,
                                        "Failed! Network Issue",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }


                            }
                        }


                }
                else{
                    Toast.makeText(this, "Re entered password is not correct", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Inputs required", Toast.LENGTH_SHORT).show()
            }
        }


        switchlog.setOnClickListener{
            val intent= Intent(this,LoginPage::class.java)
            startActivity(intent)
        }

    }


}