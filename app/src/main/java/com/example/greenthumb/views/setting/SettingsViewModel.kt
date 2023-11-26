package com.example.greenthumb.views.setting

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.models.RegisterModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsViewModel : ViewModel() {


    var isSuccess : MutableLiveData<Boolean> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()
    var message = MutableLiveData<String>()
    var firebaseImage = MutableLiveData<String>()
    var name = MutableLiveData<String>()



    public fun uploadImage(picUrl :Uri){
        val storage = FirebaseStorage.getInstance()
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        var storageReference: StorageReference = storage.reference.child("images/").child(keys)

        val uploadTask = storageReference.putFile(picUrl)
        isLoading.value=true
        uploadTask.addOnSuccessListener { taskSnapshot ->


            val downloadurl = taskSnapshot.storage.downloadUrl
            downloadurl.addOnCompleteListener {
                firebaseImage.value=it.result.toString()
            }
                .addOnFailureListener { message.value = "Image Failed" }


            isLoading.value=false

        }.addOnFailureListener {
            isLoading.value=false
            message.value="Try Again"
        }
    }

    fun updateProfile(name:String,pic:String){
        isLoading.value=true
        var database = FirebaseDatabase.getInstance();
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        database.getReference("profiles").child(keys).child("photo").setValue(pic);
        database.getReference("profiles").child(keys).child("username").setValue(name);
        isLoading.value=false
        isSuccess.value=true


    }


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