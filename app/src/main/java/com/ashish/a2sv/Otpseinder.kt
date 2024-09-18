package com.ashish.a2sv

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ashish.a2sv.databinding.ActivityOtpseinderBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class Otpseinder : AppCompatActivity() {
    private lateinit var binding: ActivityOtpseinderBinding

    private lateinit var mAuth: FirebaseAuth

    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpseinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnsend.setOnClickListener {
            val phoneNumber = binding.etPhone.text.toString().trim()
            when {
                phoneNumber.isEmpty() -> {
                    Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()

                }

                phoneNumber.length != 10 -> {
                    Toast.makeText(this, "Type valid Phone Number", Toast.LENGTH_SHORT).show()

                }

                else -> {
                    otpSend()
                }
            }

        }

    }

    private fun otpSend() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnsend.visibility = View.INVISIBLE

        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                binding.progressBar.visibility = View.GONE
                binding.btnsend.visibility = View.VISIBLE
//                Toast.makeText(this@Otpseinder, e.localizedMessage,Toast.LENGTH_SHORT).show()

            }

            override fun onCodeSent(
                verificationid: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                binding.progressBar.visibility = View.GONE
                binding.btnsend.visibility = View.VISIBLE
                Toast.makeText(this@Otpseinder, "OTP is successfully sent.", Toast.LENGTH_SHORT)
                    .show()

                val intent = Intent(this@Otpseinder, OtpVerifysActivity::class.java).apply {
                    putExtra("phone", binding.etPhone.text.toString().trim())
                    putExtra("verificationId", verificationid)

                }
                startActivity(intent)
            }
        }
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91"+ binding.etPhone.text.toString().trim())
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallback)
            .build()
    }
}

