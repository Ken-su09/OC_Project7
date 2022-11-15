package com.suonk.oc_project7.test

import android.location.Location
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

class RestaurantsViewModel : ViewModel() {

    val liveData: LiveData<List<RestaurantsViewState>> = liveData(Dispatchers.IO) {
        getLocationFlow().collectLatest { location -> // TODO NINO
            val nearbyResults = getNearbyResults("${location.latitude},${location.longitude}")
            val deferreds: List<Deferred<PlaceDetailsResponse>> = coroutineScope {
                nearbyResults.map {
                    async {
                        getPlaceDetail(it.placeId)
                    }
                }
            }
            val results: List<PlaceDetailsResponse> = deferreds.awaitAll()

            emit(
                results.map {
                    RestaurantsViewState(
                        it.placeId,
                        it.name,
                        it.openingHours.random()
                    )
                }
            )
        }
    }

    fun getLocationFlow(): Flow<Location> {
        return flowOf(Location(""))
    }

    suspend fun getNearbyResults(location: String): List<NearbyResponse> {
        delay(500)
        return listOf()
    }

    suspend fun getPlaceDetail(placeId: String): PlaceDetailsResponse {
        delay(500)
        return PlaceDetailsResponse(placeId, "", listOf())
    }

    data class NearbyResponse(
        val placeId: String,
        val name: String,
        val isOpenNow: Boolean,
    )

    data class PlaceDetailsResponse(
        val placeId: String,
        val name: String,
        val openingHours: List<String>,
    )

    data class RestaurantsViewState(
        val placeId: String,
        val name: String,
        val openingHourSentence: String,
    )
}