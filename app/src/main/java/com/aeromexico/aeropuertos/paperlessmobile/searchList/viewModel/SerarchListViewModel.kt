package com.aeromexico.aeropuertos.paperlessmobile.searchList.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aeromexico.aeropuertos.paperlessmobile.common.CORE.CoreRepository
import com.aeromexico.aeropuertos.paperlessmobile.common.utils.RequestState
import com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource.GetInfoDataSource
import com.aeromexico.aeropuertos.paperlessmobile.searchList.dataSource.SearchListDataSource
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.InsertSearchList
import com.aeromexico.aeropuertos.paperlessmobile.searchList.entities.ResponseGetInfo
import com.aeromexico.aeropuertos.paperlessmobile.searchList.repository.InfoRepository
import com.aeromexico.aeropuertos.paperlessmobile.searchList.repository.SearchRepository
import com.aeromexico.aeropuertos.paperlessmobile.webService.IAPI.GetInfoApi
import com.aeromexico.aeropuertos.paperlessmobile.webService.Responses.SearchListResponse
import com.aeromexico.aeropuertos.paperlessmobile.webService.WebServiceApi

class SerarchListViewModel: ViewModel() {
    var coreRepository: CoreRepository = CoreRepository(WebServiceApi().getCoreApi())
    var serchdataSource: SearchListDataSource = SearchRepository(WebServiceApi().getSearchListApi()).requDataSourceRepo()

    var infoRepository = InfoRepository(WebServiceApi().getInfoSearchList())
    var getInfoDataSource = infoRepository.requInfoDataSourceRepo()
    lateinit var responseGetInfo: MutableLiveData<ResponseGetInfo>
    lateinit var responseState: MutableLiveData<RequestState>

    fun insertSearchList(insertSearchList: InsertSearchList): MutableLiveData<SearchListResponse> {
        serchdataSource.insertSearchList(insertSearchList)
        return serchdataSource.responseSearchList
    }

    fun getInfo(guid: String) {
        responseState = getInfoDataSource.responseState
        responseGetInfo = getInfoDataSource.responseGetInfo
        getInfoDataSource.getInfo(guid)
    }
}