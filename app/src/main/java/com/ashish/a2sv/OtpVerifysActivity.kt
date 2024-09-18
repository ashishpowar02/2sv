package com.ashish.a2sv

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ashish.a2sv.databinding.ActivityOtpVerifysBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerifysActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerifysBinding
    private var verificationId: String? = null
    private var otpRiciver:OTPRiciver?=null
    private  lateinit var mAuth: FirebaseAuth
    private lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityOtpVerifysBinding.inflate(layoutInflater)
        setContentView(binding.root)
 mAuth = FirebaseAuth.getInstance()
        setupEditTextInput()
        binding.tvmobile.text = String.format("+91 -%s", intent.getStringExtra("phone"))
        verificationId = intent.getStringExtra("verification")


        binding.tvResendBtn.setOnClickListener{
            resendOtp()

        }
         binding.btnVerify.setOnClickListener{
             binding.progressBar.visibility = View.VISIBLE
             binding.btnVerify.visibility = View.INVISIBLE
                     if (isOtpValid()) {
                         verificationId?.let { id ->
                            val  code = getOtpCode()
                           val credential = PhoneAuthProvider.getCredential(id,code)
                           signInWithCredential(credential)


                         }

                     }else{
                         Toast.makeText(this, "OTP is not Valid!", Toast.LENGTH_SHORT).show()
                     }
         }
startAutoOtpReceiver()


    }
private  fun isOtpValid(): Boolean{
    return with(binding){
        listOf(etC1,etC2,etC3,etC4,etC5,etC6).none(){
            it.text.toString().trim().isEmpty()
        }
    }


}
private  fun getOtpCode():String{
    return with(binding )
    {
        etC1.text.toString().trim()+
                etC2.text.toString().trim()
        etC3.text.toString().trim()+
                etC4.text.toString().trim()+
                etC5.text.toString().trim()+
                etC6.text.toString().trim()
    }
}
    private fun signInWithCredential(credential: PhoneAuthCredential){
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnVerify.visibility = View.VISIBLE
                    Toast.makeText(this, "welcome..", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java).apply {
                        flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                    }
                    startActivity(intent )
                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.btnVerify.visibility = View.VISIBLE
                    Toast.makeText(this, "Otp is not Valaid!", Toast.LENGTH_SHORT).show()
                    
                }
            }
    }
private fun startAutoOtpReceiver(){
    otpRiciver= OTPRiciver().also { receiver ->
        registerReceiver(receiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION   ))
        receiver.initListener(object : OTPRiciver.OtpReceiverListener{
            override fun onOtpSuccess(otp: String) {
               binding.etC1.setText(otp[0].toString())
               binding.etC2.setText(otp[1].toString())
               binding.etC3.setText(otp[2].toString())
               binding.etC4.setText(otp[3].toString())
               binding.etC5.setText(otp[4].toString())
               binding.etC6.setText(otp[5].toString())
            }

            override fun onOtpTimeout() {
                Toast.makeText(this@OtpVerifysActivity, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}

    private fun resendOtp(){
        binding.progressBar.visibility =View.VISIBLE
        binding.btnVerify.visibility= View.INVISIBLE

        mCallbacks= object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential){

            }
            override fun onVerificationFailed(e: FirebaseException) {
                binding.progressBar.visibility = View.GONE
                binding.btnVerify.visibility = View.VISIBLE
                Toast.makeText(this@OtpVerifysActivity, "error", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificaionId: String, token: PhoneAuthProvider.ForceResendingToken) {
                binding.progressBar.visibility = View.GONE
                binding.btnVerify.visibility = View.VISIBLE
                Toast.makeText(this@OtpVerifysActivity, "OTP is successfully sent.", Toast.LENGTH_SHORT).show()
            }
        }
        val option = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91" + intent.getStringExtra("phone")!!.trim())
            .setActivity(this)
            .setCallbacks(mCallbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(option)
    }
    private fun setupEditTextInput(){
        binding.etC1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                binding.etC2.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        binding.etC2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                binding.etC3.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
            binding.etC3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                binding.etC4.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        binding.etC4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                binding.etC5.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        binding.etC5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                binding.etC6.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        otpRiciver?.let {
            unregisterReceiver(it)
        }
    }
}