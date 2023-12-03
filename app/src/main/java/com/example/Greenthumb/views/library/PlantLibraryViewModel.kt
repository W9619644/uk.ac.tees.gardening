package com.example.greenthumb.views.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenthumb.models.LibraryModel
import com.example.greenthumb.repositories.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantLibraryViewModel :ViewModel() {

    var message = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var plantDetails = MutableLiveData<ArrayList<LibraryModel>>()


     fun getPlantLibrary() {
        isLoading.value=true
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.apiService.getPlantLibrary()
            withContext(Dispatchers.Main){
                isLoading.value=false
                 if(response.isSuccessful){
                   //  message.value = "successfull"
                     plantDetails.value = response.body().let { it?.data }
                 }else{
                     message.value = "Please try again after sometime...."
                 }
            }
        }
    }
}