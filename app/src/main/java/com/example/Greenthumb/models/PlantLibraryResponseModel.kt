package com.example.greenthumb.models

data class PlantLibraryResponseModel(
    var data:ArrayList<LibraryModel>?=null
)

data class LibraryModel(
    var id:Int?=null,
    var common_name:String?=null,
    var scientific_name:String?=null,
    var year:Int?=null,
    var rank:String?=null,
    var image_url:String?=null,
    var family:String?=null
)
