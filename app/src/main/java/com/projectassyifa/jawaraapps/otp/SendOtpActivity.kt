package com.projectassyifa.jawaraapps.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.databinding.ActivitySendOtpBinding
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.pickup.layout.StatusPickupActivity
import java.util.concurrent.TimeUnit


class SendOtpActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySendOtpBinding
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var username :String? = null
    private var email  :String? = null
    private var password  :String? = null
    private var notlp  :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        println("CURRENT USER $currentUser")
        if(currentUser != null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
         username  = intent.getStringExtra(EXTRA_USERNAME)
         email  = intent.getStringExtra(EXTRA_EMAIL)
         password  = intent.getStringExtra(EXTRA_PASSWORD)

        binding.sendsms.setOnClickListener(this)

        blackIconStatusBar(this, R.color.bg);
        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                Toast.makeText(this@SendOtpActivity,"Andan sudah melakukan verifikasi sebelumnya", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token

                val intent = Intent(applicationContext,VerifyOtpActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra(VerifyOtpActivity.EXTRA_EMAIL,email)
                intent.putExtra(VerifyOtpActivity.EXTRA_USERNAME,username)
                intent.putExtra(VerifyOtpActivity.EXTRA_NOTLP,notlp)
                intent.putExtra(VerifyOtpActivity.EXTRA_PASSWORD,password)
                startActivity(intent)
            }
        }
    }
    private fun blackIconStatusBar(aktivitas: SendOtpActivity, color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(ContextCompat.getColor(aktivitas, color));
    }
    private fun sendSms() {

        var number = binding.phoneNumber.text.toString().trim()

        if(!number.isEmpty()){
            notlp = number
            number = "+62"+number
            sendVerificationcode(number)
        }else{
            Toast.makeText(this,"Mohon masukan no telepon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onClick(v: View?) {
       when(v){
           binding.sendsms->{
               sendSms()
           }
       }
    }

    companion object {
        const val EXTRA_USERNAME = "username"
        const val EXTRA_EMAIL = "email"
        const val EXTRA_PASSWORD = "password"

    }

}