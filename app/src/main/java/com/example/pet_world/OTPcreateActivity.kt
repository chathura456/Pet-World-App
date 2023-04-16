package com.example.pet_world

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPcreateActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otpcreateuser)

        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val otp= findViewById<EditText>(R.id.otp)

        auth = FirebaseAuth.getInstance()

        val otpverify= OTPVerifyActivity()

        btnVerify.setOnClickListener {
            if (otp.text.toString().trim().isNotEmpty()){
                val storedVerificationId = intent.getStringExtra("verificationId")
                Log.d(ContentValues.TAG, "otpveryfyid:$storedVerificationId")
                varifyPhoneNumberWithCode(storedVerificationId,otp.text.toString().trim())


            }
            else{
                Toast.makeText(this,"OTP verification is required", Toast.LENGTH_SHORT).show()

            }
        }
    }



    fun varifyPhoneNumberWithCode(verificationId:String?, code:String){
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)

    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(this,"welcome "+user,Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this,"Verification on failed ",Toast.LENGTH_SHORT).show()
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Verification failed invalid user ",Toast.LENGTH_SHORT).show()

                    }
                    // Update UI
                }
            }
    }


}