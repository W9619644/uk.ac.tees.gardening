package com.example.greenthumb.views.tips

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenthumb.models.TipsResponseModel
import com.example.greenthumb.repositories.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TipsArticlesViewModel : ViewModel() {

    var message = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var plantDetails = MutableLiveData<ArrayList<TipsResponseModel>>()

     fun getPlantTracker() {
        isLoading.value=true
        viewModelScope.launch(Dispatchers.IO) {
            val response = NetworkService.apiService2.getTips()
            withContext(Dispatchers.Main){
                isLoading.value=false
                if(response.isSuccessful){
                    //  message.value = "successfull"
                    plantDetails.value = response.body().let { it?.articles }
                }else{
                    message.value = "Please try again after sometime...."
                }
            }
        }
    }
}