package com.projectassyifa.jawaraapps.pickup.layout

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.ActivityMapAgentBinding
import com.projectassyifa.jawaraapps.databinding.ActivityStatusPickupBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.pickup.data.PickupRepo
import com.projectassyifa.jawaraapps.pickup.data.PickupStatusAdapter
import javax.inject.Inject

class StatusPickupActivity : AppCompatActivity() {

    @Inject
    lateinit var pickupRepo: PickupRepo
    lateinit var pickupStatusAdapter: PickupStatusAdapter

    private lateinit var tokenOuth: Token
    private lateinit var binding: ActivityStatusPickupBinding
    var dataLogin: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusPickupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = this.getSharedPreferences(
            getString(com.projectassyifa.jawaraapps.R.string.sp),
            Context.MODE_PRIVATE
        )
        val idPick = intent.getStringExtra(EXTRA_ID_PICK)
        showLoad(true)
        tokenOuth = Token(this)
        binding.statusPick.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        pickupRepo.resStatus.observe(this, Observer{
            if (it!=null) {
                pickupStatusAdapter = PickupStatusAdapter(it, this)
                binding.statusPick.adapter = pickupStatusAdapter
                showLoad(false)
            } else{
                showLoad(true)
            }
        })

        pickupRepo.getStatusPick("Bearer ${tokenOuth.fetchAuthToken()}",idPick!!,this)
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.laoding.visibility = View.VISIBLE
        }else{
            binding.laoding.visibility = View.GONE
        }
    }
    companion object {
        const val EXTRA_ID_PICK = "id_pickup"

    }
}