package com.example.greenthumb.views.login

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.gardening.databinding.ActivityLoginBinding
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity

import com.example.greenthumb.views.home.HomeActivity
import com.example.greenthumb.views.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

     lateinit var binding : ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog= ProgressDialog(this@LoginActivity)
        getPostNotificationPermission()

        binding.idRegister.setOnClickListener {
            navigateActivity(this@LoginActivity, RegisterActivity::class.java)
        }

        binding.idLogin.setOnClickListener {
            val email = binding.idUsername.text.toString() ?: ""
            val password = binding.idPassword.text.toString() ?: ""
            if(isInternetConnected(this)){
                loginViewModel.authenticate(email,password)
            }else{
                Toast.makeText(this@LoginActivity,"Please check internet connection",Toast.LENGTH_SHORT).show()
            }

        }

        loginViewModel.isLoading.observe(this@LoginActivity , Observer {
            if(it){
                dialog.setMessage("Loading....")
                dialog?.show()
            }
            else{
                if(dialog?.isShowing == true){
                    dialog.cancel()
                }
            }
        })

        // viewmodel using live data observer

        loginViewModel.isSuccess.observe(this@LoginActivity , Observer {
            if(it){
                navigateActivity(this@LoginActivity, HomeActivity::class.java)
               // finish()
            }
        })


        loginViewModel.message.observe(this@LoginActivity , Observer {
            Toast.makeText(this@LoginActivity , ""+it, Toast.LENGTH_SHORT).show()
        })

        loginViewModel.emailmessage.observe(this@LoginActivity , Observer {
            binding.idUsernameLayout.error="Enter Email ID"
            binding.idUsernameLayout.isErrorEnabled = true
          })
        loginViewModel.passmessage.observe(this@LoginActivity , Observer {
            binding.idPasswordLayout0.error="Enter valid Password"
            binding.idPasswordLayout0.isErrorEnabled = true

        })


        binding.idUsername.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
              val data = binding.idUsername.text.toString()?:""
                if(data.length>=3){
                    binding.idUsernameLayout.isErrorEnabled = false
                }
            }
        })


        binding.idPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
                val data = binding.idPassword.text.toString()?:""
                if(data.length>=3){
                    binding.idPasswordLayout0.isErrorEnabled = false
                }
            }
        })

    }

    private fun getPostNotificationPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
                requestPermissions(permissions, 1111)
            }
            else {
                // repeat the permission or open app details
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val user= FirebaseAuth.getInstance().currentUser
        if(user!=null){
            navigateActivity(this@LoginActivity, HomeActivity::class.java)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1111 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  //  openImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}