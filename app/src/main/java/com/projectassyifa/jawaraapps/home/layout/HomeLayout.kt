package com.projectassyifa.jawaraapps.home.layout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentLoginLayoutBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.maps.layout.MapAgentActivity
import com.projectassyifa.jawaraapps.reward.data.RewardAdapter
import com.projectassyifa.jawaraapps.reward.data.RewardModel
import com.projectassyifa.jawaraapps.user.data.UserModel
import com.projectassyifa.jawaraapps.user.data.UserVM
import javax.inject.Inject


class HomeLayout : Fragment() {
    private val list = ArrayList<RewardModel>()
    private lateinit var rv: RecyclerView
    private lateinit var tokenOuth: Token
    private var _binding: FragmentHomeLayoutBinding? = null
    private val binding get() = _binding!!
    var dataLogin: SharedPreferences? = null

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
        _binding = FragmentHomeLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchAgent.setOnClickListener {
            startActivity(Intent(this.activity,MapAgentActivity::class.java))
        }

        rv = binding.rewardRv
        rv.setHasFixedSize(true)
        list.addAll(listReward)
        rv.isNestedScrollingEnabled = false;
        showList()

        tokenOuth = Token(requireContext())
        val id = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        val username = dataLogin?.getString(
            getString(R.string.username),
            getString(R.string.default_value)
        )
        binding.username.text = " Hi, $username"

//        userVM.userData.observe(viewLifecycleOwner, Observer {
//            binding.username.text = " Hi, ${it.username}"
//            println("NAMA ${it.username}")
//        })
////        userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",id.toString(),requireContext())


        var banner_menu = binding.bannerSlide
        val imageList = ArrayList<SlideModel>()

       imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
       imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
       imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))

        banner_menu.setImageList(imageList)



    }

    private fun showList() {
        rv.layoutManager = LinearLayoutManager(this.requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val rewardAdapter = RewardAdapter(list)
        rv.adapter = rewardAdapter
    }
    private val listReward:ArrayList<RewardModel>
    get() {
        val name = resources.getStringArray(R.array.reward_name)
        val koin = resources.getStringArray(R.array.reward_coin)
        val image = resources.obtainTypedArray(R.array.reward_image)
        val rewardList = ArrayList<RewardModel>()
        for (position in name.indices){
            val reward = RewardModel(name[position],koin[position],image.getResourceId(position,-1))
            rewardList.add(reward)
        }
        return rewardList
    }



}

