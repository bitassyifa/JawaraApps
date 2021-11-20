package com.projectassyifa.jawaraapps.user.layout

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.databinding.FragmentHomeLayoutBinding
import com.projectassyifa.jawaraapps.databinding.FragmentUserProfilBinding
import com.projectassyifa.jawaraapps.login.layout.LoginActivity


class UserProfil : Fragment(),View.OnClickListener {
    private var _binding: FragmentUserProfilBinding? = null
    private val binding get() = _binding!!
    var dataLogin: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        _binding = FragmentUserProfilBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnLogout -> {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Konfirmasi Logout")
                builder.setMessage("Apakah anda yakin, Anda ingin Keluar Akun sekarang?")
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
        }
    }


}