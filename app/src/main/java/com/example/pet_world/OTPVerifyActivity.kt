package com.example.pet_world

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPVerifyActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val btn_getOTP = findViewById<Button>(R.id.btnGetOtp)
    val phone = findViewById<EditText>(R.id.phoneNumber)
    val btnVerify = findViewById<Button>(R.id.btnVerify)
    val otp= findViewById<EditText>(R.id.verification_code)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otpverification_activity)


        btn_getOTP.setOnClickListener {
            if (phone.text.toString().trim().isNotEmpty()){
                getOTPVerify()

            }
            else{
                Toast.makeText(this,"Phone Number is required", Toast.LENGTH_SHORT).show()
            }
        }


        btnVerify.setOnClickListener {
            if (otp.text.toString().trim().isNotEmpty()){

            }
            else{

            }
        }



    }

    private fun getOTPVerify() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone.text.toString().trim())       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save the verification id somewhere
                    // ...

                    // The corresponding whitelisted code above should be used to complete sign-in.

                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    // Sign in with the credential
                    // ...
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // ...
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}