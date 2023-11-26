package com.example.greenthumb.views.register

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.gardening.databinding.ActivityRegisterBinding
import com.example.greenthumb.common.isInternetConnected
import com.example.greenthumb.common.navigateActivity
import com.example.greenthumb.models.RegisterModel
import com.example.greenthumb.views.home.HomeActivity

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()
    lateinit var dialog : ProgressDialog
    lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog= ProgressDialog(this@RegisterActivity)
        binding.idLogin.setOnClickListener {
            val email = binding.idEmail.text.toString() ?:""
            val username = binding.idUsername.text.toString() ?:""
            val phone = binding.idPhone.text.toString() ?:""
            val confpass = binding.idCfpassword.text.toString() ?:""
            val pass = binding.idPassword.text.toString() ?:""

            val model =RegisterModel(email,username,pass,"",phone)
            if(isInternetConnected(this)){
                registerViewModel.register(model ,confpass,this@RegisterActivity)
            }else{
                Toast.makeText(this@RegisterActivity,"Please check internet connection",Toast.LENGTH_SHORT).show()
            }


        }



        binding.idRegister.setOnClickListener {
            finish()
        }

        registerViewModel.mail.observe(this@RegisterActivity, Observer {
            if(it){
                binding.idRegLayout.error="Enter Email ID"
                binding.idRegLayout.isErrorEnabled = true
            }
        })

        registerViewModel.phone.observe(this@RegisterActivity, Observer {
            if(it){
                binding.idPhonelayout.error="Enter Valid Phone Number"
                binding.idPhonelayout.isErrorEnabled = true
            }
        })

        registerViewModel.pass.observe(this@RegisterActivity, Observer {
            if(it){
                binding.idPasslayout.error="Enter Password"
                binding.idPasslayout.isErrorEnabled = true
            }
        })

         registerViewModel.cfpass.observe(this@RegisterActivity, Observer {
            if(it){
                binding.idCfpasslayout.error="MisMatch Password"
                binding.idCfpasslayout.isErrorEnabled = true
            }
        })

         registerViewModel.userName.observe(this@RegisterActivity, Observer {
            if(it){
                binding.idUsernameLayout.error="Enter Username"
                binding.idUsernameLayout.isErrorEnabled = true
            }
        })

        registerViewModel.isLoading.observe(this@RegisterActivity , Observer {
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



        binding.idUsername.addTextChangedListener(object : TextWatcher {
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

        binding.idEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
                val data = binding.idEmail.text.toString()?:""
                if(data.length>=3){
                    binding.idRegLayout.isErrorEnabled = false
                }
            }
        })



        binding.idPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
                val data = binding.idPhone.text.toString()?:""
                if(data.length>=3){
                    binding.idPhonelayout.isErrorEnabled = false
                }
            }
        })



        binding.idPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
                val data = binding.idPassword.text.toString()?:""
                if(data.length>=3){
                    binding.idPasslayout.isErrorEnabled = false
                }
            }
        })

        binding.idCfpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("","")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("","")

            }

            override fun afterTextChanged(s: Editable?) {
                val data = binding.idCfpassword.text.toString()?:""
                if(data.length>=3){
                    binding.idCfpasslayout.isErrorEnabled = false
                }
            }
        })







        // register viewmodels with livedata observer

        registerViewModel.isSuccess.observe(this@RegisterActivity , Observer {
            navigateActivity(this@RegisterActivity, HomeActivity::class.java)
            //finish()
        })

        registerViewModel.isSuccess.observe(this@RegisterActivity , Observer {
            navigateActivity(this@RegisterActivity, HomeActivity::class.java)
            //finish()
        })



        registerViewModel.message.observe(this@RegisterActivity , Observer {
            Toast.makeText(this@RegisterActivity , ""+it,Toast.LENGTH_SHORT).show()
        })
    }
}