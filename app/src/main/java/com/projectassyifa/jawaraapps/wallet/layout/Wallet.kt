package com.projectassyifa.jawaraapps.wallet.layout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentUserProfilBinding
import com.projectassyifa.jawaraapps.databinding.FragmentWalletBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.user.data.UserVM
import com.projectassyifa.jawaraapps.withdraw.layout.ListRekActivity
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject


class Wallet : Fragment(), View.OnClickListener {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    val localeID = Locale("in", "ID")
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    var dataLogin: SharedPreferences? = null
    private lateinit var tokenOuth: Token
    @Inject
    lateinit var userVM: UserVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = activity?.getSharedPreferences(
            getString(R.string.sp),
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentAdapter = AdapterLayoutWallet(childFragmentManager)
        fragmentAdapter.addFragment(Income(),"Pemasukan")
        fragmentAdapter.addFragment(Spending(),"Pengeluaran")

        binding.vp.adapter = fragmentAdapter
        binding.tabwallet.setupWithViewPager(binding.vp)
        binding.tariktunai.setOnClickListener(this)
        tokenOuth = Token(requireContext())
        val id = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        val username = dataLogin?.getString(
            getString(R.string.username),
            getString(R.string.default_value)
        )

        userVM.userData.observe(viewLifecycleOwner, Observer {
          binding.saldo.setText(formatRupiah.format(it.saldo))
        })
        userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",id.toString(),requireContext())
    }

    override fun onClick(v: View?) {
       when (v){
           binding.tariktunai ->{
               startActivity(Intent(this.requireActivity(),ListRekActivity::class.java))
           }
       }
    }


}