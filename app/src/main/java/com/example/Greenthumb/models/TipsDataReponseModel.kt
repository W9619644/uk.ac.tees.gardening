package com.example.greenthumb.models

data class TipsDataReponseModel(
    var status:String?=null,
    var totalResults:Int?=null,
    var articles:ArrayList<TipsResponseModel>?=null
)

data class TipsResponseModel(
    var source:TipsSource?=null,
  var description:String?=null,
  var title:String?=null,
  var url:String?=null,
  var urlToImage:String?=null
)

data class TipsSource(
    var  name:String?=null
)
