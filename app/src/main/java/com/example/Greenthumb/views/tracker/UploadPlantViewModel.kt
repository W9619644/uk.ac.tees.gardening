package com.example.greenthumb.views.tracker

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.greenthumb.models.UploadModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadPlantViewModel : ViewModel() {

    var isLoading = MutableLiveData<Boolean>()
    var success = MutableLiveData<Boolean>()
    var firebaseImage = MutableLiveData<String>()
    var message = MutableLiveData<String>()

    fun uploadPlant(desc: String, imgPic: String) {



        isLoading.value=true
        var database = FirebaseDatabase.getInstance().getReference("Plants");
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val uniquekey = database.root.push().key
        if (uniquekey != null) {
            val model = UploadModel(desc,imgPic,uniquekey)
            database.child(keys).child(uniquekey).setValue(model).addOnCompleteListener {
                if(it.isSuccessful){
                    message.value= "Uploaded Successfully"
                    success.value =true
                }
            }
        };
        isLoading.value=false



    }

    public fun uploadImage(picUrl : Uri){
        val storage = FirebaseStorage.getInstance()
        val keys = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val time = System.currentTimeMillis()


        var storageReference: StorageReference = storage.reference.child("ownPlants").child(keys).child(
            time.toString()
        )

        val uploadTask = storageReference.putFile(picUrl)
        isLoading.value=true
        uploadTask.addOnSuccessListener { taskSnapshot ->


            val downloadurl = taskSnapshot.storage.downloadUrl
            downloadurl.addOnCompleteListener {

                firebaseImage.value=it.result.toString()
            }
                .addOnFailureListener {
                    message.value = "Image Failed" }


            isLoading.value=false

        }.addOnFailureListener {
            isLoading.value=false
            message.value="Try Again"
        }
    }
}