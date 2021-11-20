package com.projectassyifa.jawaraapps.pickup.layout

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
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentPickupBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.pickup.data.PickupAdapter
import com.projectassyifa.jawaraapps.pickup.data.PickupRepo
import javax.inject.Inject


class Pickup : Fragment() {

    @Inject
    lateinit var pickupRepo: PickupRepo
    lateinit var pickupAdapter: PickupAdapter
    private lateinit var tokenOuth: Token

    var dataLogin: SharedPreferences? = null
    private var _binding: FragmentPickupBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentPickupBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoad(true)
        tokenOuth = Token(requireContext())
        val id_user = dataLogin?.getString(
            getString(com.projectassyifa.jawaraapps.R.string.id),
            getString(com.projectassyifa.jawaraapps.R.string.default_value)
        )
        binding.pickRv.layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        pickupRepo.resPick.observe(viewLifecycleOwner, Observer {
            if (it != null){
                pickupAdapter = PickupAdapter(it,requireActivity())
                binding.pickRv.adapter = pickupAdapter
                showLoad(false)
            }

        })
        pickupRepo.getUserPick("Bearer ${tokenOuth.fetchAuthToken()}",id_user.toString(),
            requireContext()
        )
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.laoding.visibility = View.VISIBLE
        }else{
            binding.laoding.visibility = View.GONE
        }
    }

}