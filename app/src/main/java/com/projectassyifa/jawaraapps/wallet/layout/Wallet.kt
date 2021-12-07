package com.projectassyifa.jawaraapps.wallet.layout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentUserProfilBinding
import com.projectassyifa.jawaraapps.databinding.FragmentWalletBinding


class Wallet : Fragment() {

    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }


}