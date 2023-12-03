package com.example.greenthumb.views.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivityViewModel :ViewModel() {

    var isSuccess : MutableLiveData<Boolean> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()
    var message = MutableLiveData<String>()
    var firebaseImage = MutableLiveData<String>()
    var name = MutableLiveData<String>()


    fun getDetails() {

        isLoading.value =true

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("profiles")
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""


        ref.child(keys).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post: RegisterModel = dataSnapshot.getValue(RegisterModel::class.java)!!
                firebaseImage.value= post?.photo ?: ""
                name.value= post?.username ?: ""
                isLoading.value=false

            }
            override fun onCancelled(databaseError: DatabaseError) {
                isLoading.value=false
                println("The read failed: " + databaseError.code)
            }
        })


    }
}