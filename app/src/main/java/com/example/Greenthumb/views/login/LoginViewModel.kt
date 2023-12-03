package com.example.greenthumb.views.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.common.isValidEmail
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel(){

    var isSuccess :MutableLiveData<Boolean> = MutableLiveData()
    var isLoading :MutableLiveData<Boolean> = MutableLiveData()
    var message = MutableLiveData<String>()
    var emailmessage = MutableLiveData<String>()
    var passmessage = MutableLiveData<String>()
    private val auth = FirebaseAuth.getInstance()


    public fun authenticate(email:String , password:String){

        if(isValidEmail(email)){
            if(password.length >= 6){
                 firebaseLogin(email,password)
            }else{
                passmessage.value = "Enter valid Password"
            }
        }else{
            emailmessage.value = "Enter Valid Email ID"
        }

    }

    private fun firebaseLogin(email: String, password: String) {
        isLoading.value = true
           auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
               isLoading.value = false
               if(it.isSuccessful)
               {
                   isSuccess.value=true
               }else{
                   message.value = " "+ it.exception?.message
               }
           }
    }

}