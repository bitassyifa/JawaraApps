package com.projectassyifa.jawaraapps.register.layout

import android.content.Intent
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
import com.projectassyifa.jawaraapps.databinding.FragmentRegisterLayoutBinding
import com.projectassyifa.jawaraapps.otp.SendOtpActivity
import com.projectassyifa.jawaraapps.register.data.RegisterModel
import com.projectassyifa.jawaraapps.register.data.RegisterVM
import javax.inject.Inject

class RegisterLayout : Fragment(), View.OnClickListener {

    lateinit var navController : NavController
    private var _binding: FragmentRegisterLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var registerVM: RegisterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as JawaraApps).applicationComponent.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterLayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toLogin.setOnClickListener(this)
        binding.buttonRegister.setOnClickListener(this)
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches())
                    binding.buttonRegister.isEnabled = true
                else{
                    binding.buttonRegister.isEnabled = false
                    binding.email.setError("Email not valid!")
                }
            }

        })
    }

    override fun onClick(v: View?) {
        when (v){
            binding.toLogin -> {
                view?.findNavController()?.navigate(R.id.action_global_to_loginLayout)
                navController = view?.let { Navigation.findNavController(it) }!!
            }
            binding.buttonRegister -> {
//                val dataRegist = RegisterModel(
//                    username = binding.username.text.toString(),
//                    email = binding.email.text.toString(),
//                    password = binding.password.text.toString()
//                )
                val user_msg_error: String = binding.username.text.toString()

                //check if the EditText have values or not
                if(user_msg_error.trim().isEmpty()) {
                    binding.username.error = "Required"
                    Toast.makeText(this.requireContext(), "User Name Required ", Toast.LENGTH_SHORT).show()
                }
                else if (binding.email.text.toString().trim().isEmpty()) {
                    binding.email.error = "Required"
                    Toast.makeText(this.requireContext(), "Email Required ", Toast.LENGTH_SHORT).show()
                }
                else if (binding.password.text.toString().trim().isEmpty()) {
                    binding.password.error = "Required"
                    Toast.makeText(this.requireContext(), "Password Required ", Toast.LENGTH_SHORT).show()
                } else if (binding.password.length() < 8){
                    binding.password.error = "Min 8 Character"
                }
                else if (binding.confirmPassword.text.toString().trim().isEmpty()) {
                    binding.confirmPassword.error = "Required"
                    Toast.makeText(this.requireContext(), "Password Required ", Toast.LENGTH_SHORT).show()
                }
                else if (binding.password.text.toString() != binding.confirmPassword.text.toString()){
                    binding.confirmPassword.error = "password does not match"
                    Toast.makeText(this.context,"Password does not match", Toast.LENGTH_SHORT).show()
                }
                else{
//                    registerVM.resApi.observe(viewLifecycleOwner, Observer {
//                        if (it.status){
//                            Toast.makeText(this.context,"Register Success", Toast.LENGTH_SHORT).show()
//                            view?.findNavController()?.navigate(R.id.action_global_to_loginLayout)
//                            navController = view?.let { Navigation.findNavController(it) }!!
//                        }
//                    })
//                        registerVM.register(dataRegist,requireContext())
                        val toSend = Intent(this.context,SendOtpActivity::class.java)
                        toSend.putExtra(SendOtpActivity.EXTRA_USERNAME,binding.username.text.toString())
                        toSend.putExtra(SendOtpActivity.EXTRA_EMAIL,binding.email.text.toString())
                        toSend.putExtra(SendOtpActivity.EXTRA_PASSWORD,binding.password.text.toString())
                        this.context?.startActivity(toSend)
                    }

                }
            }
        }
    }
