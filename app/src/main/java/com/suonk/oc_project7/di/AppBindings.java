package com.suonk.oc_project7.di;

import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepositoryImpl;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepositoryImpl;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public abstract class AppBindings {

    @Binds
    abstract CurrentLocationRepository bindLocationRepository(CurrentLocationRepositoryImpl impl);

    @Binds
    abstract PlacesRepository bindPlacesRepository(PlacesRepositoryImpl impl);

    @Binds
    abstract RestaurantsRepository bindRestaurantsRepository(RestaurantsRepositoryImpl impl);
}
