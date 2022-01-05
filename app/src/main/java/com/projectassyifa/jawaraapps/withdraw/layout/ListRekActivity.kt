package com.projectassyifa.jawaraapps.withdraw.layout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.ActivityListrekBinding
import com.projectassyifa.jawaraapps.databinding.ActivityStatusPickupBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.withdraw.data.AdapterListRek
import com.projectassyifa.jawaraapps.withdraw.data.WithdrawRepo
import javax.inject.Inject

class ListRekActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityListrekBinding
    private lateinit var tokenOuth: Token
    var dataLogin: SharedPreferences? = null
    @Inject
    lateinit var  withdrawRepo: WithdrawRepo
    lateinit var  adapterListRek: AdapterListRek

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListrekBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = this.getSharedPreferences(
            getString(R.string.sp),
            Context.MODE_PRIVATE
        )
        tokenOuth = Token(this)
        val id_user = dataLogin?.getString(
            getString(com.projectassyifa.jawaraapps.R.string.id),
            getString(com.projectassyifa.jawaraapps.R.string.default_value)
        )
        binding.apply {
            addAccount.setOnClickListener(this@ListRekActivity)

           listrek.layoutManager = LinearLayoutManager(this@ListRekActivity,RecyclerView.VERTICAL,false)
            withdrawRepo.resAccountBank.observe(this@ListRekActivity, Observer {
                if (it !=null){
                    adapterListRek = AdapterListRek(it,this@ListRekActivity)
                    listrek.adapter = adapterListRek
                }
            })
            withdrawRepo.getRekUser("Bearer ${tokenOuth.fetchAuthToken()}",id_user.toString(),
                this@ListRekActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v){
            binding.addAccount ->{
                startActivity(Intent(this,AddBankAccount::class.java))
            }
        }
    }
}