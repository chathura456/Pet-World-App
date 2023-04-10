package com.example.pet_world

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class OTPcreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otpcreateuser)

        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val otp= findViewById<EditText>(R.id.otp)


        val otpverify= OTPVerifyActivity()

        btnVerify.setOnClickListener {
            if (otp.text.toString().trim().isNotEmpty()){
                val storedVerificationId = intent.getStringExtra("verificationId")
                otpverify.varifyPhoneNumberWithCode(storedVerificationId,otp.text.toString().trim())


            }
            else{
                Toast.makeText(this,"OTP verification is required", Toast.LENGTH_SHORT).show()

            }
        }
    }


}