package ru.evbobrutskov.tinkoffandroiddeveloper2021.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.evbobrutskov.tinkoffandroiddeveloper2021.MainActivity
import ru.evbobrutskov.tinkoffandroiddeveloper2021.network.*

class ContentViewModel : ViewModel() {

    private val _status = MutableLiveData<DevelopersLifeApiStatus>()

    val status: LiveData<DevelopersLifeApiStatus>
        get() = _status

    private val _property = MutableLiveData<DevelopersLifeProperty>()

    val property: LiveData<DevelopersLifeProperty>
        get() = _property

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getDevelopersLifeProperties(category: String, page: Int, activity: MainActivity) {

        coroutineScope.launch {
            val getPropertiesDeferred =
                DevelopersLifeApi.retrofitService.getPropertiesAsync(category, page)

            try {
                _status.value = DevelopersLifeApiStatus.LOADING
                val listResult = getPropertiesDeferred.await()
                _status.value = DevelopersLifeApiStatus.DONE
                activity.megaCache[category]?.put(page, listResult)
                _property.value = listResult.result[0]
            } catch (e: Exception) {
                _status.value = DevelopersLifeApiStatus.ERROR
                _property.value = DevelopersLifeProperty(0, "", "", "", "")
            }
        }
    }

    fun setDevelopersLifeProperties(listResult: DevelopersLifePropertyContainer) {
        try {
            _status.value = DevelopersLifeApiStatus.DONE
            _property.value = listResult.result[0]
        } catch (e: java.lang.Exception) {
            _status.value = DevelopersLifeApiStatus.ERROR
            _property.value = DevelopersLifeProperty(0, "", "", "", "")
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}