package com.example.pet_world

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class OTPVerifyActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var phone1:String? =""

    private var storedVerificationId: String? =""

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit  var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otpverification_activity)

        auth = FirebaseAuth.getInstance()



        val btn_getOTP = findViewById<Button>(R.id.btnGetOtp)
        val phone = findViewById<EditText>(R.id.phoneNumber)


        phone1=phone.text.toString().trim()
        FirebaseApp.initializeApp(this)

        btn_getOTP.setOnClickListener {
            if (phone.text.toString().trim().isNotEmpty()){
                startPhoneVerification(phone.text.toString())

            }
            else{
                Toast.makeText(this,"Phone Number is required", Toast.LENGTH_SHORT).show()
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
                Log.d(TAG,"verification:$storedVerificationId")
                switch(verificationId,phone.text.toString())

            }


        }




    }


    fun switch(verificationId: String, phone: String) {
        val uname = intent.getStringExtra("uname")
        val email = intent.getStringExtra("email")
        val intent = Intent(this, OTPcreateActivity::class.java)
        intent.putExtra("verificationId", verificationId)
        intent.putExtra("phone", phone)
        intent.putExtra("email", email)
        intent.putExtra("uname",uname)
        startActivity(intent)
        Log.d(TAG,"verification:$verificationId")

    }


    override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    public fun startPhoneVerification(phoneNumber: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)


    }

     fun varifyPhoneNumberWithCode(verificationId:String?, code:String){
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)

    }

     fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    createUser(phone1.toString())
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    Toast.makeText(this,"welcome "+user,Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this,"Verification on failed ",Toast.LENGTH_SHORT).show()
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this,"Verification failed invalid user ",Toast.LENGTH_SHORT).show()

                    }
                    // Update UI
                }
            }
    }




     fun createUser(phone: String) {
        val db = Firebase.firestore
        val uname = intent.getStringExtra("uname")
        val email = intent.getStringExtra("email")
        val user: MutableMap<String, Any> = HashMap()
        user.put("email", email.toString())
        user.put("phone",phone )
        user.put("username",uname.toString())

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

    }

     fun updateUI(user: FirebaseUser?=auth.currentUser){
        //stratActivity(Intent(this, ActiviyName::class.java))
    }



}