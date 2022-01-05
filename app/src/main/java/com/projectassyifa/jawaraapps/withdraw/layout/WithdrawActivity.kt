package com.projectassyifa.jawaraapps.withdraw.layout

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.ActivityMapAgentBinding
import com.projectassyifa.jawaraapps.databinding.ActivityWithdrawBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.home.layout.HomeActivity
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.pickup.layout.StatusPickupActivity
import com.projectassyifa.jawaraapps.user.data.UserVM
import com.projectassyifa.jawaraapps.withdraw.data.WithdrawModel
import com.projectassyifa.jawaraapps.withdraw.data.WithdrawRepo
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class WithdrawActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityWithdrawBinding
    var dataLogin: SharedPreferences? = null
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    private lateinit var tokenOuth: Token
    var idUser : String? = null
    var nama : String? = null
    var bank : String? = null
    var norek : String? = null

    @Inject
    lateinit var withdrawRepo: WithdrawRepo

    @Inject
    lateinit var userVM: UserVM

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this.applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = this.getSharedPreferences(
            getString(R.string.sp),
            Context.MODE_PRIVATE
        )
        idUser = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        tokenOuth = Token(this)
        nama = intent.getStringExtra(EXTRA_NAMA)
        bank = intent.getStringExtra(EXTRA_BANK)
        norek = intent.getStringExtra(EXTRA_NOREK)

        binding.apply {
            userVM.userData.observe(this@WithdrawActivity, Observer {
                amount.setText(formatRupiah.format(it.saldo))
            })
            userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",idUser.toString(),this@WithdrawActivity)

            btnWD.setOnClickListener(this@WithdrawActivity)

            namaPemilik.text = nama
            rekening.text ="$bank | $norek"
            nominal.requestFocus()
            var linkFoto = "http://202.62.9.138/jawara_api/photo/bank/$bank"
            Glide.with(this@WithdrawActivity)
                .load(linkFoto)
                .centerCrop()
                .into(logobank)

            nominal.addTextChangedListener(object : TextWatcher{
                private var current = ""
                private val nonDigits = Regex("[^\\d]")
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == current){
                        Toast.makeText(this@WithdrawActivity, "Masukan Nominal", Toast.LENGTH_SHORT).show()
                    } else {
                        val a = s.toString().replace(nonDigits,"").toInt()
                        if (s.toString() != current) {

                            btnWD.isEnabled = 50000 <= a
                        }
                    }
                }
            })
        }
    }



    override fun onClick(v: View?) {
        when (v){
            binding.btnWD -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Konfirmasi Withdraw")
                builder.setMessage("Apakah anda yakin melakukan penarikan?")
                builder.setPositiveButton("Ya") { dialog, which ->
                 val wdModel = WithdrawModel(
                     id_user = idUser.toString(),
                     no_rek = norek.toString(),
                     bank = bank.toString(),
                     nama = nama.toString(),
                     nominal = binding.nominal.value.toInt(),
                     note = binding.notes.text.toString()
                 )
                    withdrawRepo.resApi.observe(this, Observer {
                        if (it.status){
                            Toast.makeText(this, "Trimakasih , Permintaan anda sedang di proses", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,HomeActivity::class.java))
                        } else {
                            Toast.makeText(this, "Terjadi kesalahan , cobalagi", Toast.LENGTH_SHORT).show()
                        }
                    })
                    withdrawRepo.postWD("Bearer ${tokenOuth.fetchAuthToken()}",wdModel)
                }
                builder.setNegativeButton("Tidak"){dialog,which ->
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
    companion object {
        const val EXTRA_NAMA = "nama"
        const val EXTRA_BANK = "bank"
        const val EXTRA_NOREK ="norek"
    }
}