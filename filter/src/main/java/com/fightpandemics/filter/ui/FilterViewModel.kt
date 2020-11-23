package com.fightpandemics.filter.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.data.model.userlocation.LocationRequest
import com.fightpandemics.core.result.Result
import com.fightpandemics.filter.domain.LocationDetailsUseCase
import com.fightpandemics.filter.domain.LocationPredictionsUseCase
import com.fightpandemics.filter.domain.UserLocationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import android.location.Location as Location1
import com.fightpandemics.core.data.model.userlocation.Location as Location2

/*
* created by Osaigbovo Odiase & Jose Li
* */
@ActivityScope
class FilterViewModel @Inject constructor(
    private val userLocationUseCase: UserLocationUseCase,
    private val locationPredictionsUseCase: LocationPredictionsUseCase,
    private val locationDetailsUseCase: LocationDetailsUseCase
) : ViewModel() {

    private var locationJob: Job? = null

    // Handle visibility properties
    var isLocationOptionsExpanded = MutableLiveData<Boolean>()
    var isFromWhomOptionsExpanded = MutableLiveData<Boolean>()
    var isTypeOptionsExpanded = MutableLiveData<Boolean>()

    // Recycler View autocomplete location variable
    var autocomplete_locations = MutableLiveData<HashMap<String, MutableList<String>>>()

    // handle on selected place event (either from recycler view or from current location button)
    var onSelectedLocation = MutableLiveData<String>()

    // Values that will go into home module
    var locationQuery = MutableLiveData<String>()
    var fromWhomFilters = MutableLiveData<List<String>>()
    var typeFilters = MutableLiveData<List<String>>()
    var latitude = MutableLiveData<Double>()
    var longitude = MutableLiveData<Double>()

    // Keep track of selected chip counts - Used for checking when to enable apply filter button
    var fromWhomCount = MutableLiveData<Int>()
    var typeCount = MutableLiveData<Int>()

    init {
        // cards should not be expanded at start
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
        // initialize data that will be sent as a FilterRequest
        locationQuery.value = ""
        fromWhomFilters.value = listOf()
        typeFilters.value = listOf()
        latitude.value = null
        longitude.value = null
        // initialize helper data
        onSelectedLocation.value = null
        fromWhomCount.value = 0
        typeCount.value = 0
    }

    fun clearLiveDataFilters() {
        locationQuery.value = ""
        fromWhomCount.value = 0
        typeCount.value = 0
        fromWhomFilters.value = null
        typeFilters.value = null
    }

    fun toggleView(optionsCardState: MutableLiveData<Boolean>) {
        optionsCardState.value = !optionsCardState.value!!
    }

    fun closeOptionCards() {
        isLocationOptionsExpanded.value = false
        isFromWhomOptionsExpanded.value = false
        isTypeOptionsExpanded.value = false
    }

    fun getFiltersAppliedCount(): Int {
        val fromWhomCount = fromWhomFilters.value?.size ?: 0
        val typeCount = typeFilters.value?.size ?: 0
        var total = fromWhomCount + typeCount
        if (locationQuery.value?.isNotBlank() == true) {
            total += 1
        }
        return total
    }

    // TODO: Do API here for getting place name from lat & lng
    fun updateCurrentLocation(location: Location1) {
        onSelectedLocation.value = "Todo: get place name from API"
        latitude.value = location.latitude
        longitude.value = location.longitude
        Timber.e("My filters : Location Manager View Model ${latitude.value}, ${longitude.value}")

        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            userLocationUseCase(
                LocationRequest(
                    location.latitude,
                    location.longitude
                )
            )
                .collect {
                    when (it) {
                        is Result.Success -> {
                            val l = it.data as Location2
                            Timber.e(l.toString())
                        }
                        is Result.Error -> {
                            Timber.e("${it.exception}")
                        }
                    }
                }
        }
    }

    // TODO: Do API here for autocomplete suggestions - address predictions
    fun autocompleteLocation(query: String) {

        locationJob?.cancel()
        if (query.isEmpty()) return
        locationJob = viewModelScope.launch {

        }
    }

    // TODO: Do API here for getting lat lng from placeId - /api/geo/location-details
    fun getLatLng(placeId: String) {

    }

    fun createFilterRequest(): FilterRequest {
        return FilterRequest(
            locationQuery.value,
            latitude.value,
            longitude.value,
            fromWhomFilters.value,
            typeFilters.value
        )
    }
}