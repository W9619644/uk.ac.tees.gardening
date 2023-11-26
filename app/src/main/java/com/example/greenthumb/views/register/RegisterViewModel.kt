package com.example.greenthumb.views.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.common.isValidEmail
import com.example.greenthumb.models.RegisterModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {

    var message = MutableLiveData<String>()
    var mail = MutableLiveData<Boolean>()
    var pass = MutableLiveData<Boolean>()
    var cfpass = MutableLiveData<Boolean>()
    var phone = MutableLiveData<Boolean>()
    var userName = MutableLiveData<Boolean>()
    var isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val auth = FirebaseAuth.getInstance()


    fun register(model: RegisterModel, confpass: String,context:Context) {

        if(model.username?.isNotEmpty() == true){


            if (model.email?.let { isValidEmail(it) } == true) {
                if (model.phone?.length == 10) {
                    if (model.password?.length!! >= 6) {
                        if (model.password != confpass) {
                            cfpass.value=true
                        } else {
                            // save user details to firebase cloud
                            registerDetails(model)
                        }
                    } else {
                        Toast.makeText(context,"Password should be greater than 6", Toast.LENGTH_SHORT).show()
                        pass.value =true


                    }
                } else {
                    Toast.makeText(context,"Phone should be greater than 10", Toast.LENGTH_SHORT).show()
                    phone.value =true

                }
            } else {
                Toast.makeText(context,"Invalid Email", Toast.LENGTH_SHORT).show()
                mail.value =true
            }

        }else{
            Toast.makeText(context,"Username cannot be empty", Toast.LENGTH_SHORT).show()
            userName.value =true
        }


    }

    private fun registerDetails(model: RegisterModel) {
        isLoading.value = true

        Log.e("manan","reached register model")
        model.password?.let {
            model.email?.let { it1 ->
                auth.createUserWithEmailAndPassword(it1, it)
                    .addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {

                            saveDataToDatabase(model)
                        } else {
                            isLoading.value = false
                            message.value = "" + task.exception?.message
                        }
                    }
            }
        }


    }

    private fun saveDataToDatabase(model: RegisterModel) {
        val database = Firebase.database
        val myRef = database.getReference("profiles")
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        keys?.let {
            myRef.child(it).setValue(model).addOnCompleteListener {
                isLoading.value = false
                if (it.isSuccessful) {
                    message.value = "Registration Successfull"
                    isSuccess.value = true

                } else {
                    message.value = "" + it.exception?.message
                }
            }

        }


    }
}