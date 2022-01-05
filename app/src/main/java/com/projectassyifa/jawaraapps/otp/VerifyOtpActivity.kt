package com.projectassyifa.jawaraapps.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.ActivitySendOtpBinding
import com.projectassyifa.jawaraapps.databinding.ActivityVerifyOtpBinding
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.register.data.RegisterModel
import com.projectassyifa.jawaraapps.register.data.RegisterVM
import javax.inject.Inject

class VerifyOtpActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityVerifyOtpBinding
    var storedVerificationId : String? = null
    private var username :String? = null
    private var email  :String? = null
    private var password  :String? = null
    private var notlp  :String? = null

    @Inject
    lateinit var registerVM: RegisterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as JawaraApps).applicationComponent.inject(this)
        showLoad(false)
        storedVerificationId = intent.getStringExtra("storedVerificationId")
        auth = FirebaseAuth.getInstance()
        username  = intent.getStringExtra(SendOtpActivity.EXTRA_USERNAME)
        email  = intent.getStringExtra(SendOtpActivity.EXTRA_EMAIL)
        password  = intent.getStringExtra(SendOtpActivity.EXTRA_PASSWORD)
        notlp = intent.getStringExtra(EXTRA_NOTLP)
        
        binding.apply {
            verifyBtn.setOnClickListener(this@VerifyOtpActivity)
            notlpVerif.text = "62+ $notlp"
        }
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    startActivity(Intent(applicationContext, LoginActivity::class.java))
//                    finish()
//                    Toast.makeText(this,"Akun telah terdaftar , silahkan login :)",Toast.LENGTH_SHORT).show()
                val dataRegist = RegisterModel(
                    username = username.toString(),
                    email = email.toString(),
                    password = password.toString(),
                    no_tlp = "0$notlp"
                )
                    registerVM.resApi.observe(this, Observer {
                        if (it.status){
                            Toast.makeText(this,"Akun telah terdaftar , silahkan login ", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        }
                    })
                    registerVM.register(dataRegist,this)

                } else {
// Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                        showLoad(false)
                        Toast.makeText(this,"kode OTP tidak valid",Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    override fun onClick(v: View?) {
        when(v){
            binding.verifyBtn ->{
                showLoad(true)
                val otp = binding.idOtp.text.toString().trim()
                if(!otp.isEmpty()){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId.toString(), otp)
                    signInWithPhoneAuthCredential(credential)

                }else{
                    showLoad(false)
                    Toast.makeText(this,"Masukan OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.laoding.visibility = View.VISIBLE
        }else{
            binding.laoding.visibility = View.GONE
        }
    }
    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASSWORD = "password"
        const val EXTRA_NOTLP = "notlp"
    }
}