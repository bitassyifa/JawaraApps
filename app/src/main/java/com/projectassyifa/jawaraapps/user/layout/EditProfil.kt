package com.projectassyifa.jawaraapps.user.layout

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.projectassyifa.jawaraapps.R
import com.projectassyifa.jawaraapps.databinding.ActivityEditProfilBinding
import com.projectassyifa.jawaraapps.databinding.ActivityMapAgentBinding
import android.graphics.drawable.ColorDrawable

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projectassyifa.jawaraapps.config.JawaraApps
import com.projectassyifa.jawaraapps.extra.Token
import com.projectassyifa.jawaraapps.home.layout.HomeActivity
import com.projectassyifa.jawaraapps.user.data.UpdateUserModel
import com.projectassyifa.jawaraapps.user.data.UserVM
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EditProfil : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditProfilBinding
    private val cameraRequestId  = 1222
    var calender = Calendar.getInstance()
    private var idUser : String? = null
    private var jkl : String? = null
    var fileImage: File? = null
    @Inject
    lateinit var userVM: UserVM
    private lateinit var tokenOuth: Token
    var dataLogin: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as JawaraApps).applicationComponent.inject(this)
        showLoad(true)
        dataLogin = this.getSharedPreferences(
            getString(com.projectassyifa.jawaraapps.R.string.sp),
            Context.MODE_PRIVATE
        )

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                cameraRequestId
            )

        idUser = dataLogin?.getString(
            getString(R.string.id),
            getString(R.string.default_value)
        )
        tokenOuth = Token(this)


        binding.apply {
            tgl.setOnClickListener(this@EditProfil)
            choseFile.setOnClickListener(this@EditProfil)
            btnSave.setOnClickListener(this@EditProfil)
            val gender = resources.getStringArray(R.array.gender)

            // access the spinner
            val spinner =  pilihGender

            val adapter = ArrayAdapter(this@EditProfil, android.R.layout.simple_spinner_item, gender)

            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    jkl = gender[position]
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
            userVM.userData.observe(this@EditProfil, androidx.lifecycle.Observer {
                if ( it != null) {
                    namaTv.setText(it.nama)
                    nikTv.setText(it.nik)
                    tgl.setText(it.tgl_lahir)
                    noTlpTv.setText(it.no_tlp)
                    adressTv.setText(it.alamat)
                    namaFile.setText(it.foto_ktp)
                    if(it.jk == "Laki-laki"){
                        pilihGender.setSelection(1)
                    }else if (it.jk == "Perempuan"){
                        pilihGender.setSelection(2)
                    }else {
                        pilihGender.setSelection(0)
                    }


                    if (it.foto_ktp != "") {
                        val linkFoto = "http://202.62.9.138/jawara_api/photo/ktp/${it.foto_ktp}"
                        Glide.with(this@EditProfil)
                            .load(linkFoto)
                            .centerCrop()
                            .into(fotoKtp)
                    } else {
                        Glide.with(this@EditProfil)
                            .load(R.drawable.ktpp)
                            .centerCrop()
                            .into(fotoKtp)
                    }

                    showLoad(false)
                }
            })
            userVM.userById("Bearer ${tokenOuth.fetchAuthToken()}",idUser.toString(),this@EditProfil)
        }
//        blackIconStatusBar(this, R.color.white);
    }
    private fun showLoad(state : Boolean){
        if (state){
            binding.load.visibility = View.VISIBLE
        }else{
            binding.load.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode,resultCode,data,this,object : DefaultCallback(){
            var selectedImage = data?.data
            override fun onImagePicked(
                imageFile: File?,
                source: EasyImage.ImageSource?,
                type: Int
            ) {
                fileImage = imageFile
                val namaImage = imageFile?.name
                binding.namaFile.setText(namaImage)
                val requestOptions = RequestOptions().error(R.drawable.error)
                Glide.with(this@EditProfil)
                    .load(imageFile)
                    .apply(requestOptions)
                    .into(binding.fotoKtp)
            }
        });
    }
//    private fun blackIconStatusBar(editProfil: EditProfil, color: Int) {
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        window.setStatusBarColor(ContextCompat.getColor(editProfil, color));
//    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
      when (v){
          binding.tgl -> {
              val mYear = calender.get(Calendar.YEAR);
              val mMonth = calender.get(Calendar.MONTH);
              val mDay = calender.get(Calendar.DAY_OF_MONTH);

              val dialog = DatePickerDialog(this@EditProfil, android.R.style.Theme_Holo_Light_Dialog,
                  { datePicker, year, month, day ->
                      binding.tgl.setText("$day - ${month+1} - $year")
                  }, mYear, mMonth, mDay
              )
              dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
              dialog.show()
          }
          binding.choseFile -> {
              val popMenu = PopupMenu(this,v)
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
          binding.btnSave -> {
              showLoad(true)
              val updateUserModel = UpdateUserModel(
                  id_user = idUser,
                  nama = binding.namaTv.text.toString(),
                  nik = binding.nikTv.text.toString(),
                  jk = jkl,
                  tgl_lahir = binding.tgl.text.toString(),
                  no_tlp = binding.noTlpTv.text.toString(),
                  foto_ktp = null,
                  alamat = binding.adressTv.text.toString()
              )
              if(fileImage == null){
                  userVM.userApi.observe(this, androidx.lifecycle.Observer {
                      if (it.status){
                          Toast.makeText(this, "Update biodata sukses", Toast.LENGTH_SHORT).show()
                          finish()
                          startActivity(Intent(this,HomeActivity::class.java))
                      }
                  })
                  userVM.userUpdateNoPhoto("Bearer ${tokenOuth.fetchAuthToken()}",updateUserModel,this
                  )
              } else {
                  userVM.userApi.observe(this, androidx.lifecycle.Observer {
                      if (it.status){
                          Toast.makeText(this, "Update biodata sukses", Toast.LENGTH_SHORT).show()
                          finish()
                          startActivity(Intent(this,HomeActivity::class.java))
                      }
                  })
                  userVM.userUpdate("Bearer ${tokenOuth.fetchAuthToken()}",updateUserModel,this,
                      fileImage!!
                  )
              }
          }
      }
    }
}