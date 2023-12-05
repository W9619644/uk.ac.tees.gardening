package com.example.greenthumb.repositories

import com.example.greenthumb.models.PlantLibraryResponseModel
import com.example.greenthumb.models.TipsDataReponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/api/v1/plants?token=YzyjFTZP3tYwVnTyC9R195i-SQEzAqINb1M2DV0NdzM")
    suspend fun getPlantLibrary() : Response<PlantLibraryResponseModel>

    @GET("/v2/everything?q=green plants&from=2023-12-2&sortBy=popularity&apiKey=7bf356b8b3b9497d83ab24b28a03283d")
    suspend fun getTips() : Response<TipsDataReponseModel>



}