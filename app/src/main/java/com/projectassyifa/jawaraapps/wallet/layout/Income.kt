package com.projectassyifa.jawaraapps.wallet.layout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.FragmentIncomeBinding
import com.projectassyifa.jawaraapps.databinding.FragmentWalletBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.wallet.data.HistoryTransAdapter
import com.projectassyifa.jawaraapps.wallet.data.WalletRepo
import javax.inject.Inject


class Income : Fragment() {
    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!
    var dataLogin: SharedPreferences? = null
    private lateinit var tokenOuth: Token

    @Inject
    lateinit var walletRepo : WalletRepo
    lateinit var historyTransAdapter: HistoryTransAdapter

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
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoad(true)
        tokenOuth = Token(requireContext())
        val id = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        binding.rvHistory.layoutManager = LinearLayoutManager(this.requireContext(),RecyclerView.VERTICAL,false)
        walletRepo.resHistory.observe(viewLifecycleOwner, Observer {
            if (it != null){
                historyTransAdapter = HistoryTransAdapter(it,this.requireActivity())
                binding.rvHistory.adapter = historyTransAdapter
                showLoad(false)
            }
        })
        walletRepo.getHistoryTransaction("Bearer ${tokenOuth.fetchAuthToken()}", id.toString(),this.requireContext())
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.laoding.visibility = View.VISIBLE
        }else{
            binding.laoding.visibility = View.GONE
        }
    }
}