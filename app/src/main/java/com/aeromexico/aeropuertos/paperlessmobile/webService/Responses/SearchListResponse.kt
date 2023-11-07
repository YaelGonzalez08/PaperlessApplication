package com.aeromexico.aeropuertos.paperlessmobile.webService.Responses


import com.google.gson.annotations.SerializedName

data class SearchListResponse(
    @SerializedName("Code")
    var code: Int = 0,
    @SerializedName("Error")
    var error: Boolean = false,
    @SerializedName("ErrorMessage")
    var errorMessage: Any = Any(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ResponseCode")
    var responseCode: String = "",
    @SerializedName("Result")
    var result: ResultSerchList = ResultSerchList(),
    @SerializedName("Status")
    var status: String = ""
)

data class SearchList(
    @SerializedName("idSearchList")
    var idSearchList: Int = 0
)

data class ResultSerchList(
    @SerializedName("SearchList")
    var searchList: SearchList = SearchList()
)