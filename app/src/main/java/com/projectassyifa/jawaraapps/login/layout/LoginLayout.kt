package com.projectassyifa.jawaraapps.login.layout

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.databinding.FragmentLoginLayoutBinding
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.home.layout.HomeLayout
import com.projectassyifa.jawaraapps.login.data.LoginModel
import com.projectassyifa.jawaraapps.login.data.LoginVM
import spencerstudios.com.bungeelib.Bungee
import javax.inject.Inject


class LoginLayout : Fragment() , View.OnClickListener {
    var dataLogin : SharedPreferences? = null
    lateinit var navController : NavController
    private var _binding: FragmentLoginLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var loginVM: LoginVM

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
        _binding = FragmentLoginLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toRegister.setOnClickListener(this)
        binding.buttonLogin.setOnClickListener(this)

        binding.email.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches())
                    binding.buttonLogin.isEnabled = true
                else{
                    binding.buttonLogin.isEnabled = false
                    binding.email.setError("Email not valid!")
                }
            }

        })
        //login validation
        if (dataLogin?.contains(getString(R.string.id))!! && dataLogin?.contains(getString(R.string.login_method_key))!!)
        {
            view.findNavController().navigate(R.id.action_global_to_homeActivity)
        }

        navController = Navigation.findNavController(view)
        loginVM.resApi.observe(viewLifecycleOwner, Observer {
            if (!it.status){
                Toast.makeText(
                    this.context,
                    "Wrong Email or Password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
//                Toast.makeText(this.context, "Login Success", Toast.LENGTH_SHORT).show()
                loginVM.loginResponse.observe(viewLifecycleOwner, Observer {
                    if (it != null){
                        with(dataLogin?.edit()){
                            this?.putString(getString(R.string.id),it.id)
                            this?.putString(getString(R.string.email),it.email)
                            this?.putString(getString(R.string.username),it.username)
                            this?.putString(getString(R.string.login_method_key),"loginData")
                            this?.commit()
                        }
                        navController.navigate(R.id.action_global_to_homeActivity)
                    }
                })
            }
        })
    }

    override fun onClick(v: View?) {
        when (v){
            binding.toRegister -> {
                view?.findNavController()?.navigate(R.id.action_global_to_registerLayout)
                navController = view?.let { Navigation.findNavController(it) }!!


            }
            binding.buttonLogin ->{
                showLoad(true)
                val auth = LoginModel(
                    email = binding.email.text.toString(),
                    password = binding.password.text.toString()
                )
                val emailErr: String = binding.email.text.toString()

                //check if the EditText have values or not
                if(emailErr.trim().isEmpty()) {
                    binding.email.error = "Required"
                    Toast.makeText(this.requireContext(), "Email Required ", Toast.LENGTH_SHORT).show()
                    showLoad(false)
                }
                else if (binding.password.text.toString().trim().isEmpty()) {
                    binding.password.error = "Required"
                    Toast.makeText(this.requireContext(), "Password Required ", Toast.LENGTH_SHORT).show()
                    showLoad(false)
                }
                else if (binding.password.length() < 8){
                    binding.password.error = "Min 8 Character"
                    showLoad(false)
                }
                else {

                    loginVM.login(auth,requireContext())
                }
            }
        }
    }
    private fun showLoad(state : Boolean){
        println(
            "MASUK SHOW $state"
        )
        if (state){
            binding.laoding.visibility = View.VISIBLE
        }else{
            binding.laoding.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}