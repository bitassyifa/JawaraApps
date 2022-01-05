package com.projectassyifa.jawaraapps.withdraw.layout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.ActivityAddBankAccountBinding
import com.projectassyifa.jawaraapps.databinding.ActivityListrekBinding
import com.projectassyifa.jawaraapps.withdraw.data.AdapterListbank
import com.projectassyifa.jawaraapps.withdraw.data.WithdrawRepo
import com.zues.searchable_spinner.SearchableSpinner
import javax.inject.Inject
import android.text.Editable
import android.text.InputFilter

import android.text.TextWatcher
import com.bumptech.glide.Glide
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.user.data.UserVM
import com.projectassyifa.jawaraapps.withdraw.data.AccountBankModel


class AddBankAccount : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAddBankAccountBinding

    private var rekNumber : String? = null
    @Inject
    lateinit var withdrawRepo: WithdrawRepo
    lateinit var adapterListbank: AdapterListbank

    private var idUser : String? = null

    @Inject
    lateinit var userVM: UserVM
    private lateinit var tokenOuth: Token
    
    var dataLogin: SharedPreferences? = null
    var bank : String ? = null
    var namaUser : String ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBankAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = this.getSharedPreferences(
            getString(com.projectassyifa.jawaraapps.R.string.sp),
            Context.MODE_PRIVATE
        )

        binding.btnSimpan.setOnClickListener(this)
        withdrawRepo.resBank.observe(this, Observer {
            adapterListbank = AdapterListbank(it,this)
            val list = adapterListbank.listbank
            val container : ArrayList<String> = ArrayList()

            list.forEach {
                container.add(it.nama_bank)
                binding.spinnerListbank.setItems(container)
            }

            binding.spinnerListbank.setOnItemSelectListener(object :SearchableSpinner.SearchableItemListener{
                override fun onItemSelected(view: View?, position: Int) {

                    bank = binding.spinnerListbank.mSelectedItem.toString()
                    Toast.makeText(
                        this@AddBankAccount,
                        bank,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSelectionClear() {

                }

            })
        })
        binding.norek.addTextChangedListener(object : TextWatcher {
            private var current = ""
            private val nonDigits = Regex("[^\\d]")
            override fun afterTextChanged(s: Editable) {
                 rekNumber = s.toString().replace(nonDigits,"")
                // apply your logic for putting space after every four characters typed
                if (s.toString() != current) {

                    if (rekNumber!!.length <= 16) {
                        current = rekNumber!!.chunked(4).joinToString(" ")
                        s.filters = arrayOfNulls<InputFilter>(0)
                    }
                    s.replace(0, s.length, current, 0, current.length)
                }
                binding.btnSimpan.isEnabled = !((rekNumber!!.length <10) || (bank == null))
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
        withdrawRepo.listbank(this)
        idUser = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        tokenOuth = Token(this)
        userVM.userData.observe(this@AddBankAccount, androidx.lifecycle.Observer {
           binding.namarek.text = it.nama
            namaUser = it.nama
        })
        userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",idUser.toString(),this@AddBankAccount)
    }

    override fun onClick(v: View?) {
      when (v){
          binding.btnSimpan ->{
              val builder = AlertDialog.Builder(this)
              builder.setTitle("Konfirmasi tambah akun")
              builder.setMessage("Apakah anda yakin  ?")
              builder.setPositiveButton("Ya") { dialog, which ->
                  val accountBankModel = AccountBankModel(
                      id_user = idUser.toString(),
                      no_rek = rekNumber.toString(),
                      bank = bank.toString(),
                      nama = namaUser.toString()
                  )
                  withdrawRepo.resApi.observe(this, Observer {
                      if (it.status){
                          Toast.makeText(this, "Sukses menambah akun bank", Toast.LENGTH_SHORT).show()
                          startActivity(Intent(this,ListRekActivity::class.java))
                      } else {
                          Toast.makeText(this, "gagal menambahkan akun bank ${it.message}", Toast.LENGTH_SHORT).show()
                      }
                  })
                  withdrawRepo.postRekUser("Bearer ${tokenOuth.fetchAuthToken()}",accountBankModel,this)
              }
              builder.setNegativeButton("Tidak"){dialog,which ->
              }
              val dialog: AlertDialog = builder.create()
              dialog.show()
          }
      }
    }
}