package com.example.pet_world

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import org.jetbrains.annotations.NotNull
import java.util.concurrent.TimeUnit

class OTPVerifyActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private var storedVerificationId: String? =""

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit  var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otpverification_activity)

        auth = FirebaseAuth.getInstance()

        val btn_getOTP = findViewById<Button>(R.id.btnGetOtp)
        val phone = findViewById<EditText>(R.id.phoneNumber)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val otp= findViewById<EditText>(R.id.verification_code)

        FirebaseApp.initializeApp(this)

        btn_getOTP.setOnClickListener {
            if (phone.text.toString().trim().isNotEmpty()){
                startPhoneVerification(phone.text.toString())

            }
            else{
                Toast.makeText(this,"Phone Number is required", Toast.LENGTH_SHORT).show()
            }
        }


        btnVerify.setOnClickListener {
            if (otp.text.toString().trim().isNotEmpty()){
                varifyPhoneNumberWithCode(storedVerificationId,phone.text.toString())

            }
            else{
                Toast.makeText(this,"OTP verification is required", Toast.LENGTH_SHORT).show()

            }
        }




        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {



                } else if (e is FirebaseTooManyRequestsException) {

                }


            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID
                storedVerificationId = verificationId
                resendToken = token
            }
        }




    }

    override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    private fun startPhoneVerification(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

    private fun varifyPhoneNumberWithCode(verificationId:String?, code:String){
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(this,"welcome "+user,Toast.LENGTH_SHORT).show()

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


    private fun updateUI(user: FirebaseUser?=auth.currentUser){
        //stratActivity(Intent(this, ActiviyName::class.java))
    }



}