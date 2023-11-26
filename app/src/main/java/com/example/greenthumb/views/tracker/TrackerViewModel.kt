package com.example.greenthumb.views.tracker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.models.UploadModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TrackerViewModel :ViewModel() {


    var isLoading = MutableLiveData<Boolean>()
    var success = MutableLiveData<Boolean>()
    var firebaseImage = MutableLiveData<String>()
    var message = MutableLiveData<String>()
    val getPlantList = MutableLiveData<ArrayList<UploadModel>>()




     fun getTrackerList() {

         isLoading.value =true

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Plants")
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""


        ref.child(keys).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                isLoading.value = false
                val arr = ArrayList<UploadModel>()
                for(snapshot in dataSnapshot.children){
                    val post: UploadModel = snapshot.getValue(UploadModel::class.java)!!
                    if(post!=null){
                        arr.add(post)
                    }
                }
                getPlantList.value = arr

            }

            override fun onCancelled(databaseError: DatabaseError) {
                isLoading.value = false
                println("The read failed: " + databaseError.code)
            }
        })


    }
}