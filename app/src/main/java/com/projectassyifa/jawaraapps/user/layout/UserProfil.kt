package com.projectassyifa.jawaraapps.user.layout

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentUserProfilBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.login.layout.LoginActivity
import com.projectassyifa.jawaraapps.maps.layout.MapAgentActivity
import com.projectassyifa.jawaraapps.user.data.UserVM
import pl.aprilapps.easyphotopicker.EasyImage
import javax.inject.Inject


class UserProfil : Fragment(),View.OnClickListener {
    @Inject
    lateinit var userVM: UserVM
    private lateinit var tokenOuth: Token
    private val cameraRequestId  = 1222
    private var _binding: FragmentUserProfilBinding? = null
    private val binding get() = _binding!!
    var dataLogin: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JawaraApps).applicationComponent.inject(this)
        dataLogin = activity?.getSharedPreferences(
            getString(R.string.sp),
            Context.MODE_PRIVATE
        )
        if (ContextCompat.checkSelfPermission(
                activity?.applicationContext as JawaraApps, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.CAMERA),
                cameraRequestId
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        view.clearFocus()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener(this)
        showLoad(true)
        val username = dataLogin?.getString(
            getString(R.string.username),
            getString(R.string.default_value)
        )
        val idUser = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        tokenOuth = Token(requireContext())
        binding.apply {

            userVM.userData.observe(viewLifecycleOwner, Observer {

                if ( it != null) {
                    nameTV.text = it.nama
                    phoneTV.text = it.no_tlp
                    if (it.sts_verifikasi == "not_verified") {
                        verification.text = "Not Verified"
                        verification.setTextColor(Color.RED)
                        Glide.with(this@UserProfil)
                            .load(R.drawable.not)
                            .centerCrop()
                            .override(25, 25)
                            .into(verifImage)
                    } else {
                        verification.text = "Verified"
                        Glide.with(this@UserProfil)
                            .load(R.drawable.yes)
                            .centerCrop()
                            .override(25, 25)
                            .into(verifImage)
                    }
                    showLoad(false)
                }
            })
            userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",idUser.toString(),requireContext())

            toEditProfil.setOnClickListener(this@UserProfil)
            chosePp.setOnClickListener(this@UserProfil)
//            profilUsername.text = username
        }
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.load.visibility = View.VISIBLE
        }else{
            binding.load.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnLogout -> {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Konfirmasi Logout")
                builder.setMessage("Apakah anda yakin keluar akun ?")
                builder.setPositiveButton("Ya") { dialog, which ->
                    with(dataLogin?.edit()) {
                        this?.clear()
                        this?.apply()
                        Intent(getContext(), LoginActivity::class.java).apply {
                            addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                        }.also { startActivity(it) }
                    }
                }

                builder.setNegativeButton("Tidak"){dialog,which ->

                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            binding.toEditProfil ->{
                startActivity(Intent(this.activity, EditProfil::class.java))
            }
            binding.chosePp ->{
                val popMenu = PopupMenu(this.activity,v)
                popMenu.setOnMenuItemClickListener { item ->
                    when(item.itemId){
                        R.id.opencamera -> {
                            EasyImage.openCamera(this,1)
                            true
                        }
                        R.id.openfile -> {
                            EasyImage.openGallery(this,1)
                            true
                        }
                        else -> false
                    }
                }
                popMenu.inflate(R.menu.upload)
                popMenu.show()
            }
        }
    }


}